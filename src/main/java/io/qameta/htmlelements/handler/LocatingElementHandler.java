package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebElementContext;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

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

        // Proxy
        if (getAllMethods(WebElement.class).contains(method)) {
            return invokeWebElementMethod(method, args, getContext().getLocator());
        }

        return super.invoke(proxy, method, args);
    }

    private Object invokeWebElementMethod(Method method, Object[] args, ElementLocator locator) throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(5));

        Throwable lasException = null;
        while (clock.isNowBefore(end)) {
            try {
                WebElement element = locator.findElement();
                return method.invoke(element, args);
            } catch (Throwable e) {
                lasException = e;
            } finally {
                Thread.sleep(250);
            }
        }
        throw lasException;
    }

}
