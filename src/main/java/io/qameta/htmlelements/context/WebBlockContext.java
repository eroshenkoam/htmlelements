package io.qameta.htmlelements.context;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;

public class WebBlockContext {

    private String name;

    private WebDriver driver;

    private WebBlockContext() {
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public WebDriver getDriver() {
        return driver;
    }

    private void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebBlockContext child(String name, String selector) {
        WebBlockContext childContext = new WebBlockContext();
        childContext.setDriver(this.getDriver());
        childContext.setName(name);
        return childContext;
    }

    public static WebBlockContext newWebPageContext(String name, WebDriver driver) {
        WebBlockContext context = new WebBlockContext();
        context.setDriver(driver);
        context.setName(name);
        return context;
    }

    public static WebBlockContext newWebPageContext(Class<?> webPageClass, WebDriver driver) {
        return newWebPageContext(splitCamelCase(webPageClass.getSimpleName()), driver);
    }

    private static String splitCamelCase(String text) {
        return Joiner.on(" ").join(StringUtils.splitByCharacterTypeCamelCase(text));
    }


}
