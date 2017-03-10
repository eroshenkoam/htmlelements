package io.qameta.htmlelements.listener;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.extension.MethodHandler;

import java.lang.reflect.Method;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public interface WebBlockListener {

    void beforeMethodHandlerInvocation(Context context, Method method, Object[] args, MethodHandler handler);

    void beforeWebElementMethodInvocation(Context context, Method method, Object[] args);

    void afterMethodInvocation(Method method, Object result);
}
