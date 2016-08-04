package io.qameta.htmlelements.handler;

import java.lang.reflect.Method;

public interface ByNameMethodCallHandler extends MethodCallHandler {

    default boolean canHandle(Method method) {
        return getHandleMethodName().equals(method.getName());
    }

    String getHandleMethodName();

}
