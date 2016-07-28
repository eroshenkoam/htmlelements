package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.decorator.MethodDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

public class LocatingElementListHandler implements InvocationHandler {

    private final MethodDecorator decorator;

    private final ClassLoader classLoader;

    private final ElementLocator locator;

    private final Class<?> returnedType;

    public LocatingElementListHandler(ElementLocator locator, Class<?> returnedType, MethodDecorator decorator, ClassLoader classLoader) {
        this.returnedType = returnedType;
        this.classLoader = classLoader;
        this.decorator = decorator;
        this.locator = locator;
    }

    public ElementLocator getLocator() {
        return locator;
    }

    public MethodDecorator getDecorator() {
        return decorator;
    }

    public Class<?> getReturnedType() {
        return returnedType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> originalElements = getLocator().findElements();

        List<Object> wrappedElements = originalElements.stream()
                .map(element -> Proxy.newProxyInstance(classLoader, new Class[]{getReturnedType()},
                        new LocatingElementHandler(element, getDecorator()))).collect(Collectors.toList());

        try {
            return method.invoke(wrappedElements, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
