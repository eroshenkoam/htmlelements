package io.qameta.htmlelements.handler;

import java.lang.reflect.Method;

public class FilterMatchedMethodCallHandler implements ByNameMethodCallHandler {
    @Override
    public String getHandleMethodName() {
        return "filter";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return proxy;
    }
}
