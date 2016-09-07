package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.hamcrest.Matcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

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
            return ((SlowLoadableComponent<Object>) () -> {
                assertThat(proxy, matcher);
                return proxy;
            }).get();
        }
    }

}
