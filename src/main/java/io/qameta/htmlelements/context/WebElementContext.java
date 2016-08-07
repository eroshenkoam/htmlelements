package io.qameta.htmlelements.context;

import org.openqa.selenium.support.pagefactory.ElementLocator;

public class WebElementContext {

    private final Class<?> webElementClass;

    private final ElementLocator locator;

    public WebElementContext(Class<?> webElementClass, ElementLocator locator) {
        this.webElementClass = webElementClass;
        this.locator = locator;
    }

    public Class<?> getWebElementClass() {
        return webElementClass;
    }

    public ElementLocator getLocator() {
        return locator;
    }

}
