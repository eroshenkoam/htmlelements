package io.qameta.htmlelements;

import io.qameta.htmlelements.handler.PageObjectHandler;
import io.qameta.htmlelements.proxy.Proxies;
import org.openqa.selenium.WebDriver;

public class WebPageFactory {

    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        return Proxies.simpleProxy(pageObjectClass, new PageObjectHandler(() -> driver));
    }

}
