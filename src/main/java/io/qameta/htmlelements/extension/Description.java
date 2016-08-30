package io.qameta.htmlelements.extension;


import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(Description.Handler.class)
@ExtendWith(Description.Extension.class)
public @interface Description {

    String DESCRIPTION_KEY = "description";

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            Map<String, Object> store = context.getStore();
            String description = method.getName();
            store.put(DESCRIPTION_KEY, description);
        }
    }

    class Handler implements MethodHandler<String> {

        @Override
        public String handle(Context context) {
            return context.getStore().get(DESCRIPTION_KEY).toString();
        }

    }
}
