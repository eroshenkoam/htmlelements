package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(Selector.Extension.class)
public @interface Selector {

    class Extension implements ContextEnricher, ContextMethodInterceptor<String> {

        private static final String SELECTOR_KEY = "selector";

        @Override
        public String intercept(Context context) {
            return context.getStore().get(SELECTOR_KEY).toString();
        }

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            Map<String, Object> store = context.getStore();
            String selector = method.getAnnotation(FindBy.class).value();
            store.put(SELECTOR_KEY, selector);
        }
    }

}
