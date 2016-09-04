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
@HandleWith(DriverProvider.Extension.class)
@ExtendWith(DriverProvider.Extension.class)
public @interface DriverProvider {

    class Extension implements ContextEnricher, MethodHandler<WebDriver> {

        private static final String DRIVER_KEY = "driver";

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            context.getParent().ifPresent(parent -> {
                WebDriver driver = (WebDriver) parent.getStore().get(DRIVER_KEY);
                context.getStore().put(DRIVER_KEY, driver);
            });
        }

        @Override
        public WebDriver handle(Context context, Object proxy, Object[] args) {
            return (WebDriver) context.getStore().get(DRIVER_KEY);
        }
    }

}
