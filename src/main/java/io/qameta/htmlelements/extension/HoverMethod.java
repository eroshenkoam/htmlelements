package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static io.qameta.htmlelements.context.Store.DRIVER_KEY;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(HoverMethod.Extension.class)
public @interface HoverMethod {

    class Extension implements MethodHandler {

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            WebDriver driver = context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));
            Actions actions = new Actions(driver);
            WebBlockMethodHandler handler = (WebBlockMethodHandler) Proxy.getInvocationHandler(proxy);
            actions.moveToElement((WebElement) handler.getUnwrappedObject()).perform();
            return proxy;
        }
    }

}
