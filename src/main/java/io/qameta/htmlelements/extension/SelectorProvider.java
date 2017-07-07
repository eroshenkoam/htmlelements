package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(SelectorProvider.Extension.class)
@ExtendWith(SelectorProvider.Extension.class)
public @interface SelectorProvider {

    class Extension implements ContextEnricher, MethodHandler<String> {

        private static final String SELECTOR_KEY = "selector";

        @Override
        @SuppressWarnings("unchecked")
        public void enrich(Context context, Method method, Object[] args) {
            Map<String, String> global = context.getStore().get(Context.PARAMETERS_KEY, Map.class)
                    .orElse(new HashMap());
            String selector = ReflectionUtils.getSelector(method, args, global);
            context.getStore().put(SELECTOR_KEY, selector);
        }

        @Override
        public String handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            return context.getStore().get(SELECTOR_KEY, String.class)
                    .orElseThrow(() -> new NoSuchElementException("Missing selector"));
        }
    }

}
