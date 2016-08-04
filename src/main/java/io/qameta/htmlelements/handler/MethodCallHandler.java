package io.qameta.htmlelements.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface MethodCallHandler extends InvocationHandler {

    boolean canHandle (Method method);

}
