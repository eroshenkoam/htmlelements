package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ToStringMethod.Extension.class)
public @interface ToStringMethod {

    class Extension implements MethodHandler<String>{

        @Override
        public String handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            String description = context.getStore().get(Context.DESCRIPTION_KEY, String.class)
                    .orElseThrow(() -> new NoSuchElementException("Missing toString"));
            return String.format("{name: %s}", description);
        }

    }
}
