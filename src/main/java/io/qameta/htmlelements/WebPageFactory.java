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
import java.util.Map;
import java.util.Properties;

import static io.qameta.htmlelements.context.Context.newWebPageContext;

public class WebPageFactory {

    private final List<Listener> listeners = new ArrayList<>();

    private final Properties properties = new Properties();

    private final Properties parameters = new Properties();

    public WebPageFactory listener(Listener listener) {
        this.listeners.add(listener);
        return this;
    }

    public WebPageFactory property(String key, String value) {
        properties.setProperty(key, value);
        return this;
    }

    public WebPageFactory parameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public WebPageFactory parameters(Map<String, String> map) {
        map.forEach(this::parameter);
        return this;
    }

    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        Context context = newWebPageContext(pageObjectClass, driver);
        context.getStore().put(Context.LISTENERS_KEY, listeners);
        context.getStore().put(Context.PROPERTIES_KEY, properties);
        if (pageObjectClass.isAnnotationPresent(BaseUrl.class)) {
            context.getStore().put(Store.BASE_URL_KEY, pageObjectClass.getAnnotation(BaseUrl.class).value());
        }
        return Proxies.simpleProxy(pageObjectClass, new WebBlockMethodHandler(context, () -> driver, WebDriver.class));
    }

}
