package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(DriverProvider.Handler.class)
@ExtendWith(DriverProvider.Extension.class)
public @interface DriverProvider {

    String DRIVER_KEY = "driver";

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            if (context.hasParent()) {
                WebDriver driver = (WebDriver) context.getParent().getStore().get(DRIVER_KEY);
                context.getStore().put(DRIVER_KEY, driver);
            }
        }
    }

    class Handler implements MethodHandler<WebDriver> {

        @Override
        public WebDriver handle(Context context) {
            return (WebDriver) context.getStore().get(DRIVER_KEY);
        }
    }

}
