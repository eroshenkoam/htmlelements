package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.water.SlowLoadableComponent;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

class LocatingElementHandler extends ComplexHandler {

    private final ElementLocator locator;

    LocatingElementHandler(ElementLocator locator) {
        this.locator = locator;
    }

    private ElementLocator getLocator() {
        return locator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> proxyClass = WebElement.class;

        if (getAllMethods(proxyClass).contains(method)) {
            return invokeProxyMethod(getLocator(), method, args);
        }

        return super.invoke(proxy, method, args);
    }

    private Object invokeProxyMethod(ElementLocator locator, Method method, Object[] args) throws Throwable {
        return ((SlowLoadableComponent<Object>) () -> {
            WebElement element = locator.findElement();
            return method.invoke(element, args);
        }).get();
    }
}
