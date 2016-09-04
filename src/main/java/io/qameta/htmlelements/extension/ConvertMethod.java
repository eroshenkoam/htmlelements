package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(ConvertMethod.Handler.class)
@ExtendWith(ConvertMethod.Extension.class)
public @interface ConvertMethod {

    String CONVERT_KEY = "convert";

    class Extension implements ContextEnricher {

        @Override
        public void enrich(Context context, Method method, Object[] args) {
            context.getStore().put(CONVERT_KEY, (Function) o -> o);
        }
    }

    class Handler implements MethodHandler {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Object[] args) {
            Function currentFunction = (Function) context.getStore().get(CONVERT_KEY);
            Function newFunction = (Function) args[0];
            context.getStore().put(CONVERT_KEY, currentFunction.andThen(newFunction));
            return proxy;
        }
    }

}
