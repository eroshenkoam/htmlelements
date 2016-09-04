package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.openqa.selenium.NoSuchElementException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(WaitUntilMethod.Extension.class)
public @interface WaitUntilMethod {

    class Extension implements MethodHandler<Object> {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Object[] args) throws Throwable {
            Predicate predicate = (Predicate) args[0];
            return ((SlowLoadableComponent<Object>) () -> {
                if (predicate.test(proxy)) {

                    return proxy;
                }
                throw new NoSuchElementException("No such element exception");
            }).get();
        }
    }

}
