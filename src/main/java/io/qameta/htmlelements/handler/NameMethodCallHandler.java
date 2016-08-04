package io.qameta.htmlelements.handler;

import java.lang.reflect.Method;

public class NameMethodCallHandler implements ByNameMethodCallHandler {

    @Override
    public String getHandleMethodName() {
        return "getName";
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
