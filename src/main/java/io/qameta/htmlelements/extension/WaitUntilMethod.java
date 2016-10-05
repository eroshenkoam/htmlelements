package io.qameta.htmlelements.extension;

import com.google.common.base.Function;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(WaitUntilMethod.Extension.class)
public @interface WaitUntilMethod {

    class Extension implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            Predicate predicate = (Predicate) args[0];
            WebDriver driver = context.getStore().get("driver", WebDriver.class)
                    .orElseThrow(() -> new RuntimeException("missing driver"));

            new WebDriverWait(driver, 5)
                    .ignoring(Throwable.class)
                    .withMessage("No such element exception")
                    .until((Function<WebDriver, Boolean>) (d) -> predicate.test(proxy));

            return proxy;
        }
    }

}
