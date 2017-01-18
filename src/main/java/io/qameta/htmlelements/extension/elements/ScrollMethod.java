package io.qameta.htmlelements.extension.elements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static io.qameta.htmlelements.context.Store.DRIVER_KEY;

/**
 * @author ehborisov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ScrollMethod.Extension.class)
public @interface ScrollMethod {

    class Extension implements MethodHandler {
        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            JavascriptExecutor driver = (JavascriptExecutor) context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));

            WebBlockMethodHandler handler = (WebBlockMethodHandler) Proxy.getInvocationHandler(proxy);
            driver.executeScript("arguments[0].scrollIntoView();", (WebElement) handler.getUnwrappedObject());
            return proxy;
        }
    }
}
