package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.*;
import io.qameta.htmlelements.decorator.MethodDecorator;
import io.qameta.htmlelements.locator.*;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

public class LocatingElementHandler implements InvocationHandler {

    private final MethodDecorator decorator;

    private final WebElementContext context;

    public LocatingElementHandler(WebElementContext context, MethodDecorator decorator) {
        this.decorator = decorator;
        this.context = context;
    }

    public MethodDecorator getDecorator() {
        return decorator;
    }

    public WebElementContext getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        WebElement element = getContext().getLocator().findElement();

        if ("toString".equals(method.getName())) {
            return String.format("Proxy for web element '%s'", getContext().getWebElementClass());
        }

        if (getAllMethods(WebElement.class).contains(method)) {
            return method.invoke(element, args);
        }

        if (getDecorator().canDecorate(method)) {
            Annotations annotations = new Annotations(method, args);
            return getDecorator().decorate(element, annotations);
        }

        throw new UnsupportedOperationException(String.format("Method '%s' is not implemented", method));
    }

}
