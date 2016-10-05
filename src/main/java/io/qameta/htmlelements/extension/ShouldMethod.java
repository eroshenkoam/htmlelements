package io.qameta.htmlelements.extension;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.hamcrest.Matcher;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ShouldMethod.Extension.class)
public @interface ShouldMethod {

    class Extension implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            Matcher matcher = (Matcher) args[0];

            WebDriver driver = context.getStore().get("driver", WebDriver.class)
                    .orElseThrow(() -> new RuntimeException("missing driver"));

            try {
                new WebDriverWait(driver, 5)
                        .ignoring(AssertionError.class)
                        .until((Predicate<WebDriver>) (d) -> {
                            assertThat(proxy, matcher);
                            return true;
                        });
            } catch (TimeoutException e) {
                throw e.getCause();
            }
            return proxy;
        }
    }

}
