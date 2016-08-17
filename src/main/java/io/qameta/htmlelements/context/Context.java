package io.qameta.htmlelements.context;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class Context {

    private Context parent;

    private String name;

    private String selector;

    private WebDriver driver;

    private Class<?> proxyClass;

    private List<Matcher> conditions;

    private Context() {
        this.conditions = new ArrayList<>();
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

    public Class<?> getProxyClass() {
        return proxyClass;
    }

    private void setProxyClass(Class<?> proxyClass) {
        this.proxyClass = proxyClass;
    }

    public List<Matcher> getConditions() {
        return conditions;
    }

    public void addCondition(Matcher condition) {
        getConditions().add(condition);
    }

    public Context newChildContext(String name, String selector, Class<?> proxyClass) {
        Context childContext = new Context();
        childContext.setDriver(this.getDriver());
        childContext.setProxyClass(proxyClass);
        childContext.setSelector(selector);
        childContext.setParent(this);
        childContext.setName(name);
        return childContext;
    }

    public static Context newWebPageContext(Class<?> webPageClass, WebDriver driver) {
        Context context = new Context();
        context.setName(splitCamelCase(webPageClass.getSimpleName()));
        context.setProxyClass(webPageClass);
        context.setDriver(driver);
        return context;
    }

    private static String splitCamelCase(String text) {
        return Joiner.on(" ").join(StringUtils.splitByCharacterTypeCamelCase(text));
    }


}
