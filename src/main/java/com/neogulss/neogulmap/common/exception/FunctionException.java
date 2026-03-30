package com.neogulss.neogulmap.common.exception;

@FunctionalInterface
public interface FunctionException<T, R, E extends Exception> {

  R apply(T t) throws E;
}