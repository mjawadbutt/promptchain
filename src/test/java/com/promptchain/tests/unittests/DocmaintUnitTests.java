package com.promptchain.tests.unittests;

import au.com.salmat.ies.ws.Document;
import au.gov.vic.luv.docmaint.service.ieswebservice.IesWebserviceMapStructMapper;
import au.gov.vic.luv.docmaint.service.ieswebservice.MappedDocument;
import au.gov.vic.luv.docmaint.service.ieswebservice.MappedDocumentObject;
import au.gov.vic.luv.docmaint.service.ieswebservice.MappedDocumentProperty;
import au.gov.vic.luv.docmaint.service.ieswebservice.MappedNote;
import au.gov.vic.luv.docmaint.service.ieswebservice.MappedOutputType;
import com.promptwise.promptchain.common.util.datetime.DateTimeConversionsUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZonedDateTime;
import java.util.List;

@DocmaintUnitTestClass
class DocmaintUnitTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocmaintUnitTests.class);

	@Test
	@DisplayName("testHelloWorld: A sample test.")
	@Tags({@Tag("Group:UnitTests")})
	void testHelloWorld() {
		LOGGER.info("Hello World!");
	}


  @Test
  @DisplayName("testIesWebserviceMapStructMapper: A sample test.")
  @Tags({@Tag("Group:UnitTests")})
  void testIesWebserviceMapStructMapper() {
    XMLGregorianCalendar xmlGregorianCalendar = DateTimeConversionsUtil.zonedDateTimeToXMLGregorianCalendar(ZonedDateTime.now());
    MappedDocument mappedAttachmentDocument = new MappedDocument(1234L, "externalIdVal", "documentTYpeValue",
            "descriptionVal", "mimeTypeVal", 10,
            List.of(new MappedDocumentProperty("docProp1Name", "docProp1Value")),
            List.of(new MappedNote(xmlGregorianCalendar, "user1", "content1")),
            null, 5678L,
            List.of(new MappedOutputType("outputType1Name", "outputType1Mime", "outputType1Description")),
            new MappedDocumentObject("mappedDocumentObjectMimeType", 15, 20, null));
    MappedDocument mappedDocument = new MappedDocument(1234L, "externalIdVal", "documentTYpeValue",
            "descriptionVal", "mimeTypeVal", 10,
            List.of(new MappedDocumentProperty("docProp1Name", "docProp1Value")),
            List.of(new MappedNote(xmlGregorianCalendar, "user1", "content1")),
            List.of(mappedAttachmentDocument), 5678L,
            List.of(new MappedOutputType("outputType1Name", "outputType1Mime", "outputType1Description")),
            new MappedDocumentObject("mappedDocumentObjectMimeType", 15, 20, null));
    Document document = IesWebserviceMapStructMapper.INSTANCE.fromMappedDocument(mappedDocument);
    MappedDocument mappedDocument1 = IesWebserviceMapStructMapper.INSTANCE.toMappedDocument(document);
    System.out.println();
  }

}
