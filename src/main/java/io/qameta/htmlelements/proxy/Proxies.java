package io.qameta.htmlelements.proxy;

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

    public static <T> T targetProxy(Class<T> classToProxy, Object target) {
        return simpleProxy(classToProxy, ((proxy, method, args) -> method.invoke(target, args)));
    }
}
