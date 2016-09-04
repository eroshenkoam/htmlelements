package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.hamcrest.MatcherAssert.assertThat;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ShouldMethod.Handler.class)
public @interface ShouldMethod {

    class Handler implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Object[] args) throws Throwable {
            Matcher matcher = (Matcher) args[0];
            return ((SlowLoadableComponent<Object>) () -> {
                assertThat(proxy, matcher);
                return proxy;
            }).get();
        }
    }

}
