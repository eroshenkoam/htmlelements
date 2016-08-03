package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebElementContext;
import io.qameta.htmlelements.decorator.MethodDecorator;
import io.qameta.htmlelements.locator.Annotations;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import org.hamcrest.Matcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.matchers.WaiterMatcherDecorator.withWaitFor;
import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

public class LocatingElementHandler implements InvocationHandler {

    private final MethodDecorator decorator;

    private final WebElementContext context;

    public LocatingElementHandler(WebElementContext context, MethodDecorator decorator) {
        this.decorator = decorator;
        this.context = context;
    }

    public WebElementContext getContext() {
        return context;
    }

    public MethodDecorator getDecorator() {
        return decorator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Proxy
        if (getAllMethods(WebElement.class).contains(method)) {
            WebElement element = getContext().getLocator().findElement();
            return method.invoke(element, args);
        }

        // Decorator
        if ("waitUntil".equals(method.getName())) {
            Matcher<WebElement> matcher = (Matcher<WebElement>) args[0];
            assumeThat((WebElement) proxy, withWaitFor(matcher));
            return proxy;
        }

        // Decorator
        if ("should".equals(method.getName())) {
            Matcher<WebElement> matcher = (Matcher<WebElement>) args[0];
            assertThat((WebElement) proxy, withWaitFor(matcher));
            return proxy;
        }

        // Decorator
        if (getDecorator().canDecorate(method)) {
            Annotations annotations = new Annotations(method, args);
            return getDecorator().decorate((SearchContext) proxy, annotations);
        }

        // Decorator
        if ("toString".equals(method.getName())) {
            return String.format("Proxy for web element '%s'", getContext().getWebElementClass());
        }

        // Last chance
        throw new UnsupportedOperationException(String.format("Method '%s.%s' is not implemented",
                getContext().getWebElementClass().getName(),
                method.getName()));
    }

}
