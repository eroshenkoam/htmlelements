package io.qameta.htmlelements.extension;

import com.google.common.base.Function;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static io.qameta.htmlelements.context.Store.DRIVER_KEY;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(WaitUntilMethod.Extension.class)
public @interface WaitUntilMethod {

    class Extension implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            String message = (String) args[0];
            Predicate predicate = (Predicate) args[1];
            WebDriver driver = context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));

            new WebDriverWait(driver, 5)
                    .ignoring(Throwable.class)
                    .withMessage(message)
                    .until((Function<WebDriver, Boolean>) (d) -> predicate.test(proxy));

            return proxy;
        }
    }

}
