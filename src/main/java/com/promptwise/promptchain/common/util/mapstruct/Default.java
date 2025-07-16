package com.promptwise.promptchain.common.util.mapstruct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Primarily for MapStruct use (to allow enforcement of immutability).
 *
 * @see <a href="https://mapstruct.org/documentation/stable/reference/html/#mapping-with-constructors"/>
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.CLASS)
public @interface Default {
}