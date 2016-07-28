package io.qameta.htmlelements.decorator;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.handler.LocatingElementHandler;
import io.qameta.htmlelements.handler.LocatingElementListHandler;
import io.qameta.htmlelements.locator.ElementLocatorFactory;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

public class DefaultMethodDecorator implements MethodDecorator {

    private final ElementLocatorFactory locatorFactory;

    private final ClassLoader classLoader;

    public DefaultMethodDecorator(ElementLocatorFactory locatorFactory, ClassLoader classLoader) {
        this.locatorFactory = locatorFactory;
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ElementLocatorFactory getLocatorFactory() {
        return locatorFactory;
    }

    @Override
    public boolean canDecorate(Method method) {
        return method.isAnnotationPresent(FindBy.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object decorate(SearchContext searchContext, Method method) {
        ElementLocator locator = getLocatorFactory().createLocator(searchContext, method);
        Class<?> returnType = method.getReturnType();

        if (WebElement.class.isAssignableFrom(returnType)) {
            return Proxy.newProxyInstance(
                    getClassLoader(),
                    new Class[]{returnType},
                    new LocatingElementHandler(locator, this)
            );
        }

        if (List.class.isAssignableFrom(returnType)) {
            Type methodReturnType = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            return Proxy.newProxyInstance(
                    getClassLoader(),
                    new Class[]{List.class},
                    new LocatingElementListHandler(locator, (Class<?>) methodReturnType, this, getClassLoader())
            );
        }

        return null;
    }
}
