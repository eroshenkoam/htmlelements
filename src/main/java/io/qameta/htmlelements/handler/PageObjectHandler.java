package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebPageContext;
import io.qameta.htmlelements.decorator.*;
import io.qameta.htmlelements.locator.*;
import org.openqa.selenium.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

public class PageObjectHandler implements InvocationHandler {

    private final MethodDecorator decorator;

    private final WebPageContext context;

    public PageObjectHandler(WebPageContext context, MethodDecorator decorator) {
        this.decorator = decorator;
        this.context = context;
    }

    private WebPageContext getContext() {
        return context;
    }

    private MethodDecorator getDecorator() {
        return decorator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Context
        if ("toString".equals(method.getName())) {
            return String.format("Proxy for web page '%s'", getContext().getWebPageClass().getName());
        }

        // WebDriver
        if (getAllMethods(WebDriver.class).contains(method)) {
            WebDriver driver = getContext().getDriver();
            return method.invoke(driver, args);
        }

        // Decorator (many decorators?) may be we can create decorator here?
        if (getDecorator().canDecorate(method)) {
            SearchContext searchContext = getContext().getDriver();
            Annotations annotations = new Annotations(method, args);
            return getDecorator().decorate(searchContext, annotations);
        }

        // Last chance
        throw new UnsupportedOperationException(String.format("Method '%s' is not implemented", method));
    }
}
