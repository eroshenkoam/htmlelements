package io.qameta.htmlelements.handler;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import io.qameta.htmlelements.context.WebElementContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

class LocatingElementListHandler implements InvocationHandler {

    private final WebElementContext context;

    LocatingElementListHandler(WebElementContext context) {
        this.context = context;
    }

    private WebElementContext getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> originalElements = getContext().getLocator().findElements();
        Class<?> returnedType = getContext().getWebElementClass();

        List<Object> wrappedElements = originalElements.stream()
                .map(element -> Proxy.newProxyInstance(getContext().getClassLoader(), new Class[]{returnedType},
                        new LocatingElementHandler(from(element, getContext()))))
                .collect(Collectors.toList());

        try {
            return method.invoke(wrappedElements, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static WebElementContext from(WebElement element, WebElementContext parent) {
        ElementLocator locator = new ElementLocator() {
            @Override
            public WebElement findElement() {
                return element;
            }

            @Override
            public List<WebElement> findElements() {
                return null;
            }
        };

        return new WebElementContext(parent.getWebElementClass(), parent.getClassLoader(), locator);
    }
}
