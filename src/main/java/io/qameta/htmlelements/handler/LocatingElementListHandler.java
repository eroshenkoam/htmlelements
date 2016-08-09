package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.proxy.Proxies;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import io.qameta.htmlelements.context.WebElementContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethods;

class LocatingElementListHandler extends ComplexHandler {

    private final WebElementContext context;

    private List originalElements;

    LocatingElementListHandler(WebElementContext context) {
        this.context = context;
    }

    private WebElementContext getContext() {
        return context;
    }

    public List getOriginalElements() {
        return originalElements;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> proxyClass = List.class;

        findOriginalElements();

        if (getMethods(proxyClass, "toString").contains(method.getName())) {
            return invokeProxyMethod(getOriginalElements(), method, args);
        }

        return super.invoke(proxy, method, args);
    }

    private void findOriginalElements() {
        Class<?> returnedType = getContext().getWebElementClass();
        if (getOriginalElements() == null) {
            originalElements = getContext().getLocator().findElements().stream()
                    .map(element -> Proxies.simpleProxy(returnedType,
                            new LocatingElementHandler(from(element))))
                    .collect(Collectors.toList());
        }
    }

    private Object invokeProxyMethod(List originalElements, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(originalElements, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static ElementLocator from(WebElement element) {
        return new ElementLocator() {
            @Override
            public WebElement findElement() {
                return element;
            }

            @Override
            public List<WebElement> findElements() {
                return null;
            }
        };
    }
}
