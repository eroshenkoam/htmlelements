package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.WebPageContext;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

public class PageObjectHandler extends ComplexHandler {

    private final WebPageContext context;

    public PageObjectHandler(WebPageContext context) {
        this.context = context;
    }

    private WebPageContext getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // WebDriver
        if ("getWrappedDriver".equals(method.getName())) {
            return getContext().getDriver();
        }

        if (getAllMethods(WebDriver.class).contains(method)) {
            WebDriver driver = getContext().getDriver();
            return method.invoke(driver, args);
        }

        return super.invoke(proxy, method, args);

    }
}
