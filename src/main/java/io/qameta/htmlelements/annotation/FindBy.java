package io.qameta.htmlelements.annotation;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.extension.ContextEnricher;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.util.ReflectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@HandleWith(FindBy.Extension.class)
public @interface FindBy {

    String value();

    class Extension implements MethodHandler<Object> {

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> proxyClass = method.getReturnType();

            // html element proxy (recurse)
            if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
                String selector = ReflectionUtils.getSelector(method, args);

                Context childContext = context.newChildContext(method, method.getReturnType());
                childContext.getRegistry().getExtensions(ContextEnricher.class)
                        .forEach(enricher -> enricher.enrich(childContext, method, args));
                return createProxy(
                        method.getReturnType(),
                        childContext,
                        () -> ((SearchContext) proxy).findElement(By.xpath(selector)),
                        WebElement.class, Locatable.class
                );
            }

            // html element list proxy (recurse)
            if (method.isAnnotationPresent(FindBy.class) && List.class.isAssignableFrom(method.getReturnType())) {
                String selector = ReflectionUtils.getSelector(method, args);

                Context childContext = context.newChildContext(method, method.getReturnType());
                childContext.getRegistry().getExtensions(ContextEnricher.class)
                        .forEach(enricher -> enricher.enrich(childContext, method, args));
                return createProxy(
                        method.getReturnType(),
                        childContext,
                        () -> {
                            List<WebElement> originalElements = ((SearchContext) proxy).findElements(By.xpath(selector));
                            Type methodReturnType = ((ParameterizedType) method
                                    .getGenericReturnType()).getActualTypeArguments()[0];
                            return (List) originalElements.stream()
                                    .map(element -> createProxy((Class<?>) methodReturnType,
                                            childContext.newChildContext(method, (Class<?>) methodReturnType),
                                            () -> element,
                                            WebElement.class, Locatable.class))
                                    .collect(toList());
                        },
                        List.class);
            }

            return null;
        }

        private <R> Object createProxy(Class<?> proxyClass, Context context, Supplier<R> supplier,
                                       Class<?>... targetClasses) {
            return Proxies.simpleProxy(
                    proxyClass, new WebBlockMethodHandler(context, supplier, targetClasses)
            );
        }

    }
}
