package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebElementContext;
import io.qameta.htmlelements.water.SlowLoadableComponent;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

class LocatingElementHandler extends ComplexHandler {

    private final WebElementContext context;

    LocatingElementHandler(WebElementContext context) {
        this.context = context;
    }

    private WebElementContext getContext() {
        return context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (getAllMethods(WebElement.class).contains(method)) {
            return invokeProxyMethod(getContext().getLocator(), method, args);
        }

        return super.invoke(proxy, method, args);
    }

    private Object invokeProxyMethod(ElementLocator locator, Method method, Object[] args) throws Throwable {
        return ((SlowLoadableComponent<Object>) () -> {
            WebElement element = locator.findElement();
            return method.invoke(element, args);
        }).get();
    }

}
