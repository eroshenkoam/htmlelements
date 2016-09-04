package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(FilterMethod.Extension.class)
@ExtendWith(FilterMethod.Extension.class)
public @interface FilterMethod {

    class Extension implements TargetModifier<List>, MethodHandler<Object> {

        private static final String FILTER_KEY = "filter";

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Object[] args) {
            Predicate filter = context.getStore().get(FILTER_KEY, Predicate.class).orElse(o -> true);
            context.getStore().put(FILTER_KEY, filter.and((Predicate) args[0]));
            return proxy;
        }

        @Override
        @SuppressWarnings("unchecked")
        public List modify(Context context, List target) {
            Predicate filter = context.getStore().get(FILTER_KEY, Predicate.class).orElse(o -> true);
            return (List) target.stream().filter(filter).collect(Collectors.toList());
        }

    }
}
