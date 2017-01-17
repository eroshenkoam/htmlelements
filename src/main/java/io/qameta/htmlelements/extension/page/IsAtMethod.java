package io.qameta.htmlelements.extension.page;

import com.google.common.base.Predicate;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.util.WebDriverUtils;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.context.Store.DRIVER_KEY;
import static java.lang.String.format;

/**
 * @author ehborisov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(IsAtMethod.Extension.class)
public @interface IsAtMethod {

    class Extension implements MethodHandler {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            Matcher<String> expectedUrlMacher = (Matcher<String>) args[0];
            WebDriver driver = context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));

            new WebDriverWait(driver, 5)
                    .ignoring(Throwable.class)
                    .withMessage(format("Couldn't wait for page with url %s to load", expectedUrlMacher))
                    .until((Predicate<WebDriver>) (d) -> (d != null && expectedUrlMacher.matches(d.getCurrentUrl())) &&
                            WebDriverUtils.pageIsLoaded(d));
            return proxy;
        }
    }
}
