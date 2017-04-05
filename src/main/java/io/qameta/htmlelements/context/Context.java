package io.qameta.htmlelements.context;

import io.qameta.htmlelements.extension.ExtensionRegistry;
import io.qameta.htmlelements.util.ReflectionUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class Context {

    public static final String DESCRIPTION_KEY = "description";

    public static final String LISTENERS_KEY = "listeners";

    public static final String DRIVER_KEY = "driver";

    private Context parent;

    private Store store;

    private ExtensionRegistry registry;

    private Context() {
        this.store = new DefaultStore();
    }

    public Optional<Context> getParent() {
        return Optional.ofNullable(parent);
    }

    private void setParent(Context parent) {
        this.parent = parent;
    }

    public Store getStore() {
        return store;
    }

    public ExtensionRegistry getRegistry() {
        return registry;
    }

    private void setRegistry(ExtensionRegistry registry) {
        this.registry = registry;
    }

    public Context newChildContext(Method method, Class<?> proxyClass) {
        Context childContext = new Context();
        childContext.getStore().put(DESCRIPTION_KEY, ReflectionUtils.getDescription(method, null));
        childContext.setRegistry(ExtensionRegistry.create(proxyClass));
        childContext.getRegistry().registerExtensions(method);
        childContext.setParent(this);
        //extension
        getStore().get(LISTENERS_KEY, List.class).ifPresent(listeners -> {
            childContext.getStore().put(LISTENERS_KEY, listeners);
        });
        getStore().get(DRIVER_KEY, WebDriver.class).ifPresent(driver -> {
            childContext.getStore().put(DRIVER_KEY, driver);
        });
        return childContext;
    }

    public static Context newWebPageContext(Class<?> webPageClass, WebDriver driver) {
        Context context = new Context();
        context.setRegistry(ExtensionRegistry.create(webPageClass));
        context.getStore().put(DESCRIPTION_KEY, ReflectionUtils.getDescription(webPageClass));
        context.getStore().put(DRIVER_KEY, driver);
        return context;
    }


}
