package io.qameta.htmlelements.context;

import org.openqa.selenium.WebDriver;

public class WebPageContext {

    private final Class<?> webPageClass;

    private final WebDriver driver;

    public WebPageContext(Class<?> webPageClass, WebDriver driver) {
        this.webPageClass = webPageClass;
        this.driver = driver;
    }

    public Class<?> getWebPageClass() {
        return webPageClass;
    }

    public WebDriver getDriver() {
        return driver;
    }

}