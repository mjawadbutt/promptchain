package com.promptwise.promptchain.common.util;

@FunctionalInterface
public interface SupplierWithException<T, E extends Exception> {

  T get() throws E;

}
