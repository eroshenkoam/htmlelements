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
@HandleWith(DescriptionProvider.Extension.class)
@ExtendWith(DescriptionProvider.Extension.class)
public @interface DescriptionProvider {

    class Extension implements ContextEnricher, MethodHandler<String> {

        private static final String DESCRIPTION_KEY = "description";

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            Map<String, Object> store = context.getStore();
            String description = method.getName();
            store.put(DESCRIPTION_KEY, description);
        }

        @Override
        public String handle(Context context, Object proxy, Object[] args) {
            return context.getStore().get(DESCRIPTION_KEY).toString();
        }
    }

}
