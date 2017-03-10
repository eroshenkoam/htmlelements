package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import org.hamcrest.Matcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ShouldMethod.Extension.class)
public @interface ShouldMethod {

    class Extension implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Invoke should method: " + Arrays.toString(args));
            String message = (String) args[0];
            Matcher matcher = (Matcher) args[1];

            assertThat(message, proxy, matcher);
            return proxy;
        }
    }

}
