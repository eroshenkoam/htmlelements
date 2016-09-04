package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.Predicate;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(FilterMethod.Handler.class)
@ExtendWith(FilterMethod.Extension.class)
public @interface FilterMethod {

    String FILTER_KEY = "filter";

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            context.getStore().put(FILTER_KEY, (Predicate) o -> true);
        }
    }

    class Handler implements MethodHandler {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Object[] args) {
            Predicate currentPredicate = (Predicate) context.getStore().get(FILTER_KEY);
            Predicate newPredicate = (Predicate) args[0];
            context.getStore().put(FILTER_KEY, currentPredicate.and(newPredicate));
            return proxy;
        }
    }

}
