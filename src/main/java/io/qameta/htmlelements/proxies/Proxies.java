package io.qameta.htmlelements.proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Proxies {

    private Proxies() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T simpleProxy(Class<T> classToProxy, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(
                classToProxy.getClassLoader(),
                new Class[]{classToProxy},
                handler
        );
    }
}
