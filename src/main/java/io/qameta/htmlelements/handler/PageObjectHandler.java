package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebPageContext;
import io.qameta.htmlelements.decorator.MethodDecorator;
import io.qameta.htmlelements.locator.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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

        if ("toString".equals(method.getName())) {
            return String.format("Proxy for web page '%s'", getContext().getWebPageClass().getName());
        }

        if ("getDriver".equals(method.getName())) {
            return getContext().getDriver();
        }

        if (getDecorator().canDecorate(method)) {
            Annotations annotations = new Annotations(method, args);
            return getDecorator().decorate(getContext().getDriver(), annotations);
        }

        throw new UnsupportedOperationException(String.format("Method '%s' is not implemented", method));
    }
}
