package io.qameta.htmlelements.context;

import io.qameta.htmlelements.extension.ExtensionRegistry;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {

    private Context parent;

    private Map<String, Object> store;

    private ExtensionRegistry registry;

    private Context() {
        this.store = new HashMap<>();
    }

    public Optional<Context> getParent() {
        return Optional.ofNullable(parent);
    }

    public boolean hasParent() {
        return parent != null;
    }

    private void setParent(Context parent) {
        this.parent = parent;
    }

    public Map<String, Object> getStore() {
        return store;
    }

    public ExtensionRegistry getRegistry() {
        return registry;
    }

    private void setRegistry(ExtensionRegistry registry) {
        this.registry = registry;
    }

    public Context newChildContext(Class<?> proxyClass) {
        Context childContext = new Context();
        childContext.setRegistry(ExtensionRegistry.create(proxyClass));
        childContext.setParent(this);
        return childContext;
    }

    public static Context newWebPageContext(Class<?> webPageClass, WebDriver driver) {
        Context context = new Context();
        context.setRegistry(ExtensionRegistry.create(webPageClass));
        context.getStore().put("driver", driver);
        return context;
    }


}
