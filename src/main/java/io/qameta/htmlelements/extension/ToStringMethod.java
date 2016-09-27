package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ToStringMethod.Extension.class)
@ExtendWith(ToStringMethod.Extension.class)
public @interface ToStringMethod {

    class Extension implements MethodHandler<String>, ContextEnricher {

        private static final String TO_STRING_KEY = "toString";

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            String description = ReflectionUtils.getDescription(method, args);

            context.getStore().put(TO_STRING_KEY, String.format("{name: %s}", description));
        }

        @Override
        public String handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            return context.getStore().get(TO_STRING_KEY, String.class)
                    .orElseThrow(() -> new NoSuchElementException("Missing toString"));
        }

    }
}
