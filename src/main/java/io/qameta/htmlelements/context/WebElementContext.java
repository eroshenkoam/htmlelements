package io.qameta.htmlelements.context;

import org.openqa.selenium.support.pagefactory.ElementLocator;

public class WebElementContext {

    private final ClassLoader classLoader;

    private final Class<?> webElementClass;

    private final ElementLocator locator;

    public WebElementContext(Class<?> webElementClass, ClassLoader classLoader, ElementLocator locator) {
        this.webElementClass = webElementClass;
        this.classLoader = classLoader;
        this.locator = locator;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Class<?> getWebElementClass() {
        return webElementClass;
    }

    public ElementLocator getLocator() {
        return locator;
    }

}
