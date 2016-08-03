package io.qameta.htmlelements;

import io.qameta.htmlelements.context.WebPageContext;
import io.qameta.htmlelements.decorator.DefaultMethodDecorator;
import io.qameta.htmlelements.decorator.MethodDecorator;
import io.qameta.htmlelements.handler.PageObjectHandler;
import io.qameta.htmlelements.locator.DefaultElementLocatorFactory;
import io.qameta.htmlelements.locator.ElementLocatorFactory;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.*;

public class WebPageFactory {

    private final WebDriver driver;

    private ClassLoader classLoader;

    public WebPageFactory(WebDriver driver, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.driver = driver;
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected ClassLoader getClassLoader() {
        return classLoader;
    }

    @SuppressWarnings("unchecked")
    public <T extends WebPage> T get(Class<T> pageObjectClass) {
        ElementLocatorFactory locatorFactory = new DefaultElementLocatorFactory();
        MethodDecorator decorator = new DefaultMethodDecorator(locatorFactory, getClassLoader());
        WebPageContext context = new WebPageContext(pageObjectClass, getDriver());

        return (T) Proxy.newProxyInstance(
                getClassLoader(),
                new Class[]{pageObjectClass},
                new PageObjectHandler(context, decorator));
    }

}
