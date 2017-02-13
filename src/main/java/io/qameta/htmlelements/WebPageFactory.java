package io.qameta.htmlelements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.extension.page.BaseUrl;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import io.qameta.htmlelements.listener.ListenerManager;
import io.qameta.htmlelements.listener.WebBlockListener;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.util.ReflectionUtils;
import io.qameta.htmlelements.util.ServiceLoaderUtils;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.qameta.htmlelements.context.Context.newWebPageContext;

public class WebPageFactory {

    private Class<? extends WebBlockListener> defaultListener;
    private boolean disableServiceLoader = false;
    private List<WebBlockListener> globalListeners = new ArrayList<>();

    public void disableServiceLoader(boolean flag) {
        disableServiceLoader = flag;
    }

    public void setDefaultListenerClass(Class<? extends WebBlockListener> listener) {
        defaultListener = listener;
    }

    public void addGlobalListener(WebBlockListener listener) {
        globalListeners.add(listener);
    }

    public <T extends WebPage> T get(WebDriver driver, Class<T> pageObjectClass) {
        Context context = newWebPageContext(pageObjectClass, driver);
        if (pageObjectClass.isAnnotationPresent(BaseUrl.class)) {
            context.getStore().put(Store.BASE_URL_KEY, pageObjectClass.getAnnotation(BaseUrl.class).value());
        }

        ListenerManager listenerManager = new ListenerManager();
        if (Objects.nonNull(defaultListener)) {
            listenerManager.addListener(ReflectionUtils.newInstance(defaultListener));
        }

        if (!disableServiceLoader) {
            List<WebBlockListener> listeners = ServiceLoaderUtils.load(this.getClass().getClassLoader(),
                    WebBlockListener.class);
            listenerManager.addListeners(listeners);
        }
        listenerManager.addListeners(globalListeners);
        context.getStore().put(Store.LISTENER_MANAGER_KEY, listenerManager);

        return Proxies.simpleProxy(pageObjectClass, new WebBlockMethodHandler(context, () -> driver, WebDriver.class));
    }

}
