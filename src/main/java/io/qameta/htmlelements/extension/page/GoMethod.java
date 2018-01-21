package io.qameta.htmlelements.extension.page;

import com.google.common.base.Function;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.util.WebDriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.context.Store.BASE_URL_KEY;
import static io.qameta.htmlelements.context.Store.DRIVER_KEY;

/**
 * @author ehborisov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(GoMethod.Extension.class)
public @interface GoMethod {

    class Extension implements MethodHandler {

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            String url = (String) context.getStore().get(BASE_URL_KEY)
                    .orElseThrow(() -> new WebPageException("BaseUrl annotation is not declared for this web page"));
            WebDriver driver = context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));
            driver.get(url);

            new WebDriverWait(driver, 5)
                    .ignoring(Throwable.class)
                    .withMessage(String.format("Couldn't wait for page with url %s to load", url))
                    .until((Function<? super WebDriver, Boolean>) d ->
                            (d != null && d.getCurrentUrl().equals(url)) &&
                            WebDriverUtils.pageIsLoaded(d));
            return proxy;
        }
    }
}
