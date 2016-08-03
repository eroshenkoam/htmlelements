package io.qameta.htmlelements.decorator;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.WebElementContext;
import io.qameta.htmlelements.handler.LocatingElementHandler;
import io.qameta.htmlelements.handler.LocatingElementListHandler;
import io.qameta.htmlelements.locator.ElementLocatorFactory;
import io.qameta.htmlelements.locator.Annotations;

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
    public Object decorate(SearchContext searchContext, Annotations annotations) {
        ElementLocator locator = getLocatorFactory().createLocator(searchContext, annotations);
        Class<?> returnType = annotations.getMethod().getReturnType();

        if (WebElement.class.isAssignableFrom(returnType)) {
            WebElementContext context = new WebElementContext(returnType, classLoader, locator);
            return Proxy.newProxyInstance(
                    getClassLoader(),
                    new Class[]{returnType},
                    new LocatingElementHandler(context, this)
            );
        }

        if (List.class.isAssignableFrom(returnType)) {
            Type methodReturnType = ((ParameterizedType) annotations.getMethod()
                    .getGenericReturnType()).getActualTypeArguments()[0];
            WebElementContext context = new WebElementContext((Class<?>) methodReturnType, classLoader, locator);
            return Proxy.newProxyInstance(
                    getClassLoader(),
                    new Class[]{List.class},
                    new LocatingElementListHandler(context, this)
            );
        }

        return null;
    }
}
