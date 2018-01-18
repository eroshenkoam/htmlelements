package io.qameta.htmlelements.extension.elements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
 * Created by igor.martynov on 1/18/18.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(UnwrappedWebElement.Extension.class)
public @interface UnwrappedWebElement {

    class Extension implements MethodHandler {
        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            WebBlockMethodHandler handler = (WebBlockMethodHandler) Proxy.getInvocationHandler(proxy);
            return handler.getUnwrappedObject();
        }
    }
}
