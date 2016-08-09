package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.element.HtmlElementList;
import io.qameta.htmlelements.proxy.Proxies;
import org.hamcrest.Matcher;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import io.qameta.htmlelements.context.WebElementContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethods;

class LocatingElementListHandler extends ComplexHandler {

    private final WebElementContext context;

    LocatingElementListHandler(WebElementContext context, Matcher... matchers) {
        this.context = context;
    }

    private WebElementContext getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> proxyClass = List.class;

        if (getMethods(proxyClass, "toString").contains(method.getName())) {
            return invokeProxyMethod(getContext().getLocator(), method, args);
        }

        if ("filter".equals(method.getName())) {
            Matcher[] matchers = (Matcher[]) args[0];
            return Proxies.simpleProxy(HtmlElementList.class, new LocatingElementListHandler(context, matchers));
        }

        return super.invoke(proxy, method, args);
    }

    private List findWebElements (ElementLocator locator, Class<?> returnedType, Matcher... matchers) {
        List<WebElement> originalElements = locator.findElements();

        return originalElements.stream()
                .filter(element -> Arrays.stream(matchers).allMatch(matcher -> matcher.matches(element)))
                .map(element -> Proxies.simpleProxy(returnedType,
                        new LocatingElementHandler(from(element))))
                .collect(Collectors.toList());

    }

    private Object invokeProxyMethod(ElementLocator locator, Method method, Object[] args) throws Throwable {
        List elements = findWebElements(locator, getContext().getWebElementClass());
        try {
            return method.invoke(elements, args);
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
