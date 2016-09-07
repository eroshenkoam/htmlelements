package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.reflect.Method;

public interface MethodHandler<R> {

    R handle(Context context, Object proxy, Method method, Object[] args) throws Throwable;

}
