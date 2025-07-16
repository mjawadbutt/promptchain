package com.promptwise.promptchain.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnumHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(EnumHelper.class);

//  public static <T extends Enum<T>> T getEnumFromValue(String value, Class<T> enumClass) throws EnumParseException {
//    Assert.notNull(enumClass, "The parameter 'enumClass' cannot be null!");
//
//    Method[] methods = enumClass.getDeclaredMethods();
//    Method customValueGetterMethod = null;
//    for (Method method : methods) {
//      if (method.isAnnotationPresent(CustomValueGetterMethod.class)) {
//        if (Modifier.isStatic(method.getModifiers())) {
//          throw new EnumHelperException(String.format("""
//                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
//                          This annotation can be used on only one method within an Enum, and this method must be a
//                          NON_STATIC METHOD that takes no parameters and returns a String""",
//                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
//        } else if (method.getParameterCount() != 0) {
//          throw new EnumHelperException(String.format("""
//                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
//                          This method must NOT take ANY PARAMETERS.""",
//                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
//        } else if (String.class != method.getReturnType()) {
//          throw new EnumHelperException(String.format("""
//                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
//                          This annotation can be used on only one method within an Enum, and this method must be a
//                          non-static method that takes no parameters and RETURNS A STRING""",
//                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
//        } else if (customValueGetterMethod != null) {
//          throw new EnumHelperException(String.format("""
//                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
//                          This annotation can be used on ONLY ONE method within an Enum, and this method must be a
//                          non-static method that takes no parameters and returns a String""",
//                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
//        } else {
//          customValueGetterMethod = method;
//        }
//      }
//    }
//
//    if (customValueGetterMethod == null) {
//      try {
//        customValueGetterMethod = enumClass.getMethod("name");
//      } catch (NoSuchMethodException e) {
//        throw new EnumHelperException("Unable to find the Java language defined instance method: 'name()' on an Enum class!", e);
//      }
//    }
//
//    T[] enumConstants = enumClass.getEnumConstants();
//    try {
//      T matchingEnumConstant = null;
//      for (T t : enumConstants) {
//        String valueForEnumConstant = (String) customValueGetterMethod.invoke(t);
//        if (Objects.equals(value, valueForEnumConstant)) {
//          if (matchingEnumConstant == null) {
//            matchingEnumConstant = t;
//          } else {
//            throw EnumParseException.createForNonUniqueValues(value, enumClass);
//          }
//        }
//      }
//      if (matchingEnumConstant == null) {
//        throw EnumParseException.createForInvalidValue(value, enumClass);
//      } else {
//        return matchingEnumConstant;
//      }
//    } catch (IllegalAccessException | InvocationTargetException e) {
//      throw new EnumHelperException(
//              String.format("Unable to invoke the value-getter-method: %s on the Enum: %s! See cause for details",
//                      customValueGetterMethod.getName(), enumClass.getName()), e);
//    }
//  }

  public static <T extends Enum<T>> List<String> getAllValues(Class<T> enumClass) {
    Objects.requireNonNull(enumClass, "The parameter 'enumClass' cannot be null!");

    T[] enumConstants = enumClass.getEnumConstants();
    List<String> values = new ArrayList<>();

    for (T constant : enumConstants) {
      String value = getValueFromEnum(constant);
      values.add(value);
    }

    return values;
  }

  public static <T extends Enum<T>> T getEnumFromValue(String value, Class<T> enumClass) throws EnumParseException {
    return getEnumFromValue(value, enumClass, false);
  }

  public static <T extends Enum<T>> T getEnumFromValue(String value, Class<T> enumClass, boolean ignoreCase) throws EnumParseException {
    Method customValueGetterMethod = resolveCustomValueGetterMethod(enumClass);
    T[] enumConstants = enumClass.getEnumConstants();

    try {
      T matchingEnumConstant = null;
      for (T constant : enumConstants) {
        String resolvedValue = (String) customValueGetterMethod.invoke(constant);
        String resolvedValueToUse = ignoreCase ? resolvedValue.toUpperCase() : resolvedValue;
        String valueToUse = ignoreCase ? value.toUpperCase() : value;
        if (Objects.equals(valueToUse, resolvedValueToUse)) {
          if (matchingEnumConstant == null) {
            matchingEnumConstant = constant;
          } else {
            throw EnumParseException.createForNonUniqueValues(value, enumClass);
          }
        }
      }

      if (matchingEnumConstant == null) {
        throw EnumParseException.createForInvalidValue(value, enumClass);
      }

      return matchingEnumConstant;
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new EnumHelperException(
              String.format("Failed to invoke method '%s' on enum '%s'", customValueGetterMethod.getName(), enumClass), e);
    }
  }

  public static <T extends Enum<T>> String getValueFromEnum(T enumConstant) {
    if (enumConstant == null) {
      return null;
    }

    Class<?> enumClass = enumConstant.getDeclaringClass();
    Method customValueGetterMethod = resolveCustomValueGetterMethod(enumClass);

    try {
      return (String) customValueGetterMethod.invoke(enumConstant);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new EnumHelperException(String.format(
              "Unable to invoke value-getter-method: '%s' on enum constant: '%s'.",
              customValueGetterMethod.getName(), enumConstant), e);
    }
  }

  private static Method resolveCustomValueGetterMethod(Class<?> enumClass) {
    Method[] methods = enumClass.getDeclaredMethods();
    Method customValueGetterMethod = null;

    for (Method method : methods) {
      if (method.isAnnotationPresent(CustomValueGetterMethod.class)) {
        if (Modifier.isStatic(method.getModifiers())) {
          throw new EnumHelperException(String.format("""
                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
                          This annotation must be on a NON_STATIC method that returns a String.""",
                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
        } else if (String.class != method.getReturnType()) {
          throw new EnumHelperException(String.format("""
                          Invalid use of the annotation: '%s' on the method: '%s' within the Enum: '%s'!
                          Method must return a String.""",
                  CustomValueGetterMethod.class.getName(), method, enumClass.getName()));
        } else if (customValueGetterMethod != null) {
          throw new EnumHelperException(String.format("""
                          Multiple methods in Enum: '%s' are annotated with '%s'. Only one is allowed.""",
                  enumClass.getName(), CustomValueGetterMethod.class.getName()));
        } else {
          customValueGetterMethod = method;
        }
      }
    }

    if (customValueGetterMethod == null) {
      try {
        return enumClass.getMethod("name");
      } catch (NoSuchMethodException e) {
        throw new EnumHelperException("Missing built-in 'name()' method on enum class!", e);
      }
    }

    return customValueGetterMethod;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface CustomValueGetterMethod {
  }

  public static class EnumHelperException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private EnumHelperException(String message) {
      this(message, null);
    }

    private EnumHelperException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static class EnumParseException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private EnumParseException(String message) {
      super(message);
    }

    private static EnumParseException createForNonUniqueValues(String value, Class<? extends Enum> enumClass) {
      return new EnumParseException(String.format("""
              Unable to parse the specified value: %s for the enum: %s because it is assigned to more than one
              enum-constant and cannot uniquely identify a single enum-constant!""", value, enumClass.getName()));
    }

    private static EnumParseException createForInvalidValue(String value, Class<? extends Enum> enumClass) {


      return new EnumParseException(String.format("""
                      The value '%s' is not valid for enum '%s'. Valid values are: %s""",
              value, enumClass.getName(), EnumHelper.getAllValues(enumClass)));
    }
  }
}
