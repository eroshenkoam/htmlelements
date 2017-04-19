package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.statement.RetryStatement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * eroshenkoam
 * 22.03.17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(Retry.Extension.class)
public @interface Retry {

    int timeoutInSeconds() default 5;

    int poolingInMillis() default 250;

    Class<? extends Throwable>[] ignoring() default {};

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            if (method.isAnnotationPresent(Retry.class)) {
                Retry retry = method.getAnnotation(Retry.class);
                context.getStore().get(Context.PROPERTIES_KEY, Properties.class).ifPresent(properties -> {
                    properties.setProperty(RetryStatement.TIMEOUT_KEY, retry.timeoutInSeconds() + "");
                    properties.setProperty(RetryStatement.POLLING_KEY, retry.poolingInMillis() + "");
                });
            }
        }
    }

}
