package io.qameta.htmlelements;

import io.qameta.htmlelements.context.WebPageContext;
import io.qameta.htmlelements.handler.PageObjectHandler;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Proxy;

public class WebPageFactory {

    private ClassLoader classLoader;

    public WebPageFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected ClassLoader getClassLoader() {
        return classLoader;
    }

    @SuppressWarnings("unchecked")
    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        WebPageContext context = new WebPageContext(pageObjectClass, driver);

        return (T) Proxy.newProxyInstance(
                getClassLoader(),
                new Class[]{pageObjectClass},
                new PageObjectHandler(context));
    }

}
