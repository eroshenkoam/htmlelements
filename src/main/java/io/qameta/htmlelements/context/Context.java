package io.qameta.htmlelements.context;

import io.qameta.htmlelements.util.ReflectionUtils;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private String name;

    private String selector;

    private WebDriver driver;

    private Context parent;

    private Map<String, Object> store;

    private Context() {
        this.store = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    private void setSelector(String selector) {
        this.selector = selector;
    }

    public WebDriver getDriver() {
        return driver;
    }

    private void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public Context getParent() {
        return parent;
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

    public Context newChildContext(String name, String selector, Class<?> proxyClass) {
        Context childContext = new Context();
        childContext.setDriver(this.getDriver());
        childContext.setSelector(selector);
        childContext.setParent(this);
        childContext.setName(name);
        return childContext;
    }

    public static Context newWebPageContext(Class<?> webPageClass, WebDriver driver) {
        Context context = new Context();
        context.setName(ReflectionUtils.getName(webPageClass));
        context.setDriver(driver);
        return context;
    }


}
