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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@HandleWith(FindBy.Extension.class)
public @interface FindBy {

    String value();

    @SuppressWarnings("unchecked")
    class Extension implements MethodHandler<Object> {

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> proxyClass = method.getReturnType();

            // html element proxy (recurse)
            if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
                Map<String, String> global = context.getStore().get(Context.PARAMETERS_KEY, Map.class)
                        .orElse(new HashMap());
                String selector = ReflectionUtils.getSelector(method, args, global);

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
                Map<String, String> global = context.getStore().get(Context.PARAMETERS_KEY, Map.class)
                        .orElse(new HashMap());
                String selector = ReflectionUtils.getSelector(method, args, global);

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
                            return IntStream.range(0, originalElements.size())
                                    .mapToObj(i -> createProxy((Class<?>) methodReturnType, ((Supplier<Context>) () -> {
                                                Context childCont = context.newChildContext(method, (Class<?>) methodReturnType);
                                                childCont.getStore().put("selector", selector + "[" + (i + 1) + "]");
                                                return childCont;
                                            }).get(),
                                            () -> originalElements.get(i),
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
