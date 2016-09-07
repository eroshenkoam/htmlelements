package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(HoverMethod.Extension.class)
public @interface HoverMethod {

    class Extension implements MethodHandler {

        private static final String DRIVER_KEY = "driver";

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            context.getStore().get(DRIVER_KEY, WebDriver.class).ifPresent(driver -> {
                Actions actions = new Actions(driver);
                actions.moveToElement((WebElement) proxy).perform();
            });
            return proxy;
        }
    }

}
