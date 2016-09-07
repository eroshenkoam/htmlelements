package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.ContextEnricher;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.util.ReflectionUtils;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethodsNames;
import static java.util.stream.Collectors.toList;

public class WebBlockMethodHandler<T> implements InvocationHandler {

    private final Supplier<T> targetProvider;

    private final Context context;

    private final Class<T> targetClass;

    public WebBlockMethodHandler(Context context, Class<T> targetClass, Supplier<T> targetProvider) {
        this.targetProvider = targetProvider;
        this.targetClass = targetClass;
        this.context = context;
    }

    private Class<T> getTargetClass() {
        return targetClass;
    }

    private Supplier<T> getTargetProvider() {
        return this.targetProvider;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<T> targetClass = getTargetClass();

        // web element proxy
        if (getMethodsNames(targetClass).contains(method.getName())) {
            return invokeTargetMethod(getTargetProvider(), method, args);
        }

        Class<?> proxyClass = method.getReturnType();

        // html element proxy (recurse)
        if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
            String name = ReflectionUtils.getName(method, args);
            String selector = ReflectionUtils.getSelector(method, args);

            Context childContext = getContext().newChildContext(method.getReturnType());
            childContext.getRegistry().getExtensions(ContextEnricher.class)
                    .forEach(enricher -> enricher.enrich(childContext, method, args));
            return createProxy(method.getReturnType(), WebElement.class, childContext, () ->
                    ((SearchContext) proxy).findElement(By.xpath(selector))
            );
        }

        // html element list proxy (recurse)
        if (method.isAnnotationPresent(FindBy.class) && List.class.isAssignableFrom(method.getReturnType())) {
            String name = ReflectionUtils.getName(method, args);
            String selector = ReflectionUtils.getSelector(method, args);

            Context childContext = getContext().newChildContext(method.getReturnType());
            childContext.getRegistry().getExtensions(ContextEnricher.class)
                    .forEach(enricher -> enricher.enrich(childContext, method, args));
            return createProxy(method.getReturnType(), List.class, childContext, () -> {
                List<WebElement> originalElements = ((SearchContext) proxy).findElements(By.xpath(selector));
                Type methodReturnType = ((ParameterizedType) method
                        .getGenericReturnType()).getActualTypeArguments()[0];
                return (List) originalElements.stream()
                        .map(element -> createProxy((Class<?>) methodReturnType,
                                WebElement.class,
                                //здесь у нас нет description и selector
                                childContext.newChildContext((Class<?>) methodReturnType),
                                () -> element))
                        .collect(toList());
            });
        }

        return getContext().getRegistry().getHandler(method)
                .orElseThrow(() -> new NotImplementedException(method))
                .handle(getContext(), proxy, method, args);
    }

    @SuppressWarnings("unchecked")
    private Object invokeTargetMethod(Supplier<T> targetProvider, Method method, Object[] args)
            throws Throwable {
        return ((SlowLoadableComponent<Object>) () -> {
            Object target = targetProvider.get();
            for (TargetModifier<Object> modifier : getContext().getRegistry().getExtensions(TargetModifier.class)) {
                target = modifier.modify(getContext(), target);
            }
            return method.invoke(target, args);
        }).get();
    }

    private <R> Object createProxy(Class<?> proxyClass, Class<R> targetClass,
                                   Context context, Supplier<R> supplier) {
        return Proxies.simpleProxy(
                proxyClass, new WebBlockMethodHandler<>(context, targetClass, supplier)
        );
    }

}
