package io.qameta.htmlelements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.extension.page.BaseUrl;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.statement.Listener;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.htmlelements.context.Context.newWebPageContext;

public class WebPageFactory {

    private final List<Listener> listeners = new ArrayList<>();

    public WebPageFactory listener(Listener listener) {
        this.listeners.add(listener);
        return this;
    }

    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        Context context = newWebPageContext(pageObjectClass, driver);
        context.getStore().put(Context.LISTENERS_KEY, listeners);
        if (pageObjectClass.isAnnotationPresent(BaseUrl.class)) {
            context.getStore().put(Store.BASE_URL_KEY, pageObjectClass.getAnnotation(BaseUrl.class).value());
        }
        return Proxies.simpleProxy(pageObjectClass, new WebBlockMethodHandler(context, () -> driver, WebDriver.class));
    }

}
