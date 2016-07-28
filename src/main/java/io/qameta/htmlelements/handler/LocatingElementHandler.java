package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.decorator.MethodDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

public class LocatingElementHandler implements InvocationHandler {

    private final MethodDecorator decorator;

    private final WebElement originalElement;

    public LocatingElementHandler(ElementLocator locator, MethodDecorator decorator) {
        this.originalElement = locator.findElement();
        this.decorator = decorator;
    }

    public LocatingElementHandler(WebElement originalElement, MethodDecorator decorator) {
        this.originalElement = originalElement;
        this.decorator = decorator;
    }

    public MethodDecorator getDecorator() {
        return decorator;
    }

    public WebElement getOriginalElement() {
        return originalElement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("toString".equals(method.getName())) {
            return String.format("Proxy for web element '%s'", getOriginalElement());
        }

        if (getAllMethods(WebElement.class).contains(method)) {
            return method.invoke(getOriginalElement(), args);
        }

        if (getDecorator().canDecorate(method)) {
            return getDecorator().decorate(getOriginalElement(), method);
        }

        throw new UnsupportedOperationException(String.format("Method '%s' is not implemented", method));
    }

}
