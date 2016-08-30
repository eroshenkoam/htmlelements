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
@HandleWith(Selector.Handler.class)
@ExtendWith(Selector.Extension.class)
public @interface Selector {

    String SELECTOR_KEY = "selector";

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            Map<String, Object> store = context.getStore();
            String selector = method.getAnnotation(FindBy.class).value();
            store.put(SELECTOR_KEY, selector);
        }
    }

    class Handler implements MethodHandler<String> {

        @Override
        public String handle (Context context) {
            return context.getStore().get(SELECTOR_KEY).toString();
        }

    }

}
