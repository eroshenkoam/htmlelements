package io.qameta.htmlelements.handler;

import java.lang.reflect.Method;

public interface MethodCallHandler {

    boolean canHandle(Method method);

    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

}
