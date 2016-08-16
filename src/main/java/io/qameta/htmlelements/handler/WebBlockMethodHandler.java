package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.WebBlockContext;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.water.SlowLoadableComponent;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethods;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;

public class WebBlockMethodHandler<T> extends ComplexHandler {

    private final Supplier<T> targetProvider;

    private final WebBlockContext context;

    private final Class<T> targetClass;

    public WebBlockMethodHandler(Class<T> targetClass, WebBlockContext context, Supplier<T> targetProvider) {
        this.targetProvider = targetProvider;
        this.targetClass = targetClass;
        this.context = context;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    private Supplier<T> getTargetProvider() {
        return this.targetProvider;
    }

    public WebBlockContext getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // default
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }

        Class<T> targetClass = getTargetClass();

        // web element proxy
        if (getMethods(targetClass, "toString").contains(method.getName())) {
            return invokeTargetMethod(getTargetProvider(), method, args);
        }

        // context
        if ("getName".equals(method.getName())) {
            return getContext().getName();
        }

        // context
        if ("getWrappedDriver".equals(method.getName())) {
            return getContext().getDriver();
        }

        // extensions
        if ("should".equals(method.getName())) {
            return invokeShouldMethod(proxy, method, args);
        }

        // finder extension
        if ("waitUntil".equals(method.getName())) {
            return invokeWaitUntilMethod(proxy, method, args);
        }

        Class<?> proxyClass = method.getReturnType();
        String name = method.getName();
        String selector = method.getAnnotation(FindBy.class).value();
        WebBlockContext childContext = getContext().child(name, selector);

        // html element proxy (recurse)
        if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
            return createProxy(method.getReturnType(), WebElement.class, childContext, () ->
                    ((SearchContext) proxy).findElement(By.xpath(selector)));
        }

        // html element list proxy (recurse)
        if (method.isAnnotationPresent(FindBy.class) && List.class.isAssignableFrom(method.getReturnType())) {
            return createProxy(method.getReturnType(), List.class, childContext, () -> {
                List<WebElement> originalElements = ((SearchContext) proxy).findElements(By.xpath(selector));
                Type methodReturnType = ((ParameterizedType) method
                        .getGenericReturnType()).getActualTypeArguments()[0];
                return (List) originalElements.stream()
                        .map(element -> createProxy((Class<?>) methodReturnType, WebElement.class, childContext, () -> element))
                        .collect(toList());
            });
        }

        throw new NotImplementedException(method);
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
    }

    private Object invokeTargetMethod(Supplier<T> targetProvider, Method method, Object[] args) throws Throwable {
        return ((SlowLoadableComponent<Object>) () -> {
            T target = targetProvider.get();
            return method.invoke(target, args);
        }).get();
    }

    private Object invokeShouldMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher matcher = (Matcher) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            assertThat(proxy, matcher);
            return proxy;
        }).get();
    }

    public Object invokeWaitUntilMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher matcher = (Matcher) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            if (matcher.matches(proxy)) {
                return proxy;
            }
            throw new NoSuchElementException("No such element exception");
        }).get();
    }

    private <R> Object createProxy(Class<?> proxyClass, Class<R> targetClass,
                                   WebBlockContext context, Supplier<R> supplier) {
        return Proxies.simpleProxy(
                proxyClass,
                new WebBlockMethodHandler<>(targetClass, context, supplier)
        );
    }

}
