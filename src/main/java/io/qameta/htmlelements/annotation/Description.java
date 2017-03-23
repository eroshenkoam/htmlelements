package io.qameta.htmlelements.annotation;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.extension.ContextEnricher;
import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.ExtendWith;
import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ExtendWith(Description.Extension.class)
public @interface Description {

    String value();

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            String description = ReflectionUtils.getDescription(method, args);
            context.getStore().put(Context.DESCRIPTION_KEY, description);
        }

    }

}
