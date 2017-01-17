package io.qameta.htmlelements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.extension.page.BaseUrl;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import io.qameta.htmlelements.proxy.Proxies;
import org.openqa.selenium.WebDriver;

import static io.qameta.htmlelements.context.Context.newWebPageContext;

public class WebPageFactory {

    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        Context context = newWebPageContext(pageObjectClass, driver);
        if(pageObjectClass.isAnnotationPresent(BaseUrl.class)){
            context.getStore().put(Store.BASE_URL_KEY, pageObjectClass.getAnnotation(BaseUrl.class).value());
        }
        return Proxies.simpleProxy(pageObjectClass, new WebBlockMethodHandler(context, () -> driver, WebDriver.class));
    }

}
