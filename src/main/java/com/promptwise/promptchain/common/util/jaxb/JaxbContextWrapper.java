package com.promptwise.promptchain.common.util.jaxb;

import com.promptwise.promptchain.common.exception.LuvCommonLibSystemException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.SchemaOutputResolver;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * A class that allows for creating a type-safe wrapper of a top-level JAXBContext (i.e. JAXBContext of a class
 * that is annotated as a root element) and provides convenience methods for the common JAXB operations such as
 * marhsalling/unmarshalling, schema based validation, and schema generation.
 */
public class JaxbContextWrapper<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(JaxbContextWrapper.class);

  /**
   * A JAXB annotated class of a root (i.e. top-level) element.
   */
  private final Class<? extends T> clazz;

  /**
   * The location of the schema (xsd file) to be used for validating the corresponding instances (xml files). It must
   * be a path within the package hierarchy visible to the class loader.
   * <p/>
   * In case the URL is absolute (i.e. it begins with a '/') then the fully qualified path must be specified.
   * Otherwise, the path is considered as relative to the package of the class on which the getResource() method
   * is invoked.
   * <p/>
   * For example if a file "abc.xsd" is in net.fbdms.iflowserver.myxsd package and getResource is invoked on this class
   * then the absolute URL for it would be "/net/fbdms/iflowserver/myxsd/abc.xsd" and the relative URL would be relative
   * to this class i.e. "../myxsd/abc.xsd".
   */
  private final URL schemaUrl;

  /**
   * Local location of the Schema File (.xsd). If provided, it will be used for validation during marshalling.
   */
  private final String schemaLocation;

  private final JAXBContext jaxbContext;

  /**
   * Represents a set of constraints that can be checked/ enforced against an XML document.
   */
  private Schema schema;

  public JaxbContextWrapper(Class<? extends T> clazz, URL schemaUrl, String schemaLocation) {
    this.clazz = clazz;
    this.schemaUrl = schemaUrl;
    this.schemaLocation = schemaLocation;

    //-- Initialize the JAXB Context.
    try {
      jaxbContext = JAXBContext.newInstance(this.clazz);
    } catch (JAXBException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while " +
              " initializing the JAXB context for the class: '" + this.clazz.getName() + "'", e);
    }

    if (this.schemaUrl == null) {
      LOGGER.warn("The schemaUrl has not been specified. " +
              "Schema based validation will not be performed during marshalling and unmarshalling operations.");
    } else {
      //-- Initialize the schema for this JAXB context
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      try {
        schema = sf.newSchema(this.schemaUrl);
      } catch (SAXException se) {
        //-- Since schema based validation is optional and, as per the JAXB specification, can be turned off by
        //-- calling setSchema() with a 'null' value. So we just log a warning and let the schema be 'null'.
        LOGGER.warn("The Schema could not be loaded from the location: \"" + this.schemaUrl +
                "\" Schema based validation will not be performed during marshalling and unmarshalling operations.", se);
      }
    }
  }

  /**
   * Marshalls the specified instance of 'T' to a String and returns it.
   *
   * @param t The instance of 'T' to be marshalled.
   */
  public String marshal(T t) {
    try {
      Marshaller marshaller = createMarshaller();
      StringWriter sw = new StringWriter();
      marshaller.marshal(t, sw);
      return sw.toString();
    } catch (JAXBException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while" +
              " marshalling an instance of '" + getClazz().getName() + "'", e);
    }
  }

  /**
   * Unmarshalls an instance of 'T' from the specified XML file and returns it.
   *
   * @param xml The xml to unmarshall
   * @return The unmarshalled instance of 'T'.
   */
  public T unmarshal(String xml) {
    try {
      Unmarshaller unmarshaller = createUnmarshaller();
      T t = (T) unmarshaller.unmarshal(new StringReader(xml));
      return t;
    } catch (JAXBException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while" +
              " unmarshalling an instance of '" + getClazz().getName() + "' from the given XML: '" + xml + "'", e);
    }
  }

  /**
   * Generates the schema, as per the JAXB annotations within the class which this JAXB Context represents, and
   * returns it.
   */
  public String generateSchema() {
    try {
      StringWriter sw = new StringWriter();
      getJaxbContext().generateSchema(new SchemaOutputResolver() {
        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) {
          //-- The parameters to this method are passed as hints (for generating an output location) by the
          //-- JAXB framework and need not be used. We use our own strategy for specifying the file to which the
          //-- generated schema will be written.
          return new StreamResult(sw);
        }
      });
      return sw.toString();
    } catch (IOException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while" +
              " generating an XSD schema file for the class: '" + getClazz().getName() + "'", e);
    }
  }

  private Marshaller createMarshaller() {
    try {
      Marshaller marshaller = getJaxbContext().createMarshaller();
      //-- The property JAXB_FORMATTED_OUTPUT specifies whether the marshalled XML data is formatted with
      //-- line-feeds and indentation, or not.
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, getSchemaLocation());
      marshaller.setSchema(getSchema());
      return marshaller;
    } catch (JAXBException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while" +
              " creating a JAXB marshaller for the class: '" + getClazz().getName() + "'", e);
    }
  }

  private Unmarshaller createUnmarshaller() {
    try {
      Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
      unmarshaller.setSchema(getSchema());
      return unmarshaller;
    } catch (JAXBException | RuntimeException e) {
      throw LuvCommonLibSystemException.create("An exception occurred while" +
              " creating a JAXB unmarshaller for the class: '" + getClazz().getName() + "'", e);
    }
  }

  private Class<? extends T> getClazz() {
    return clazz;
  }

  private URL getSchemaUrl() {
    return schemaUrl;
  }

  private String getSchemaLocation() {
    return schemaLocation;
  }

  private JAXBContext getJaxbContext() {
    return jaxbContext;
  }

  private Schema getSchema() {
    return schema;
  }

}
