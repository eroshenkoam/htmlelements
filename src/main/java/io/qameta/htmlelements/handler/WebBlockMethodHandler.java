package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.proxy.Proxies;
import io.qameta.htmlelements.util.ReflectionUtils;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethods;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;

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

        // default
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }

        //context
        if (Context.class.equals(method.getReturnType())) {
            return getContext();
        }

        Class<T> targetClass = getTargetClass();

        // web element proxy
        if (getMethods(targetClass).contains(method.getName())) {
            return invokeTargetMethod(getTargetProvider(), method, args);
        }

        // extension
        if ("waitUntil".equals(method.getName())) {
            return invokeWaitUntilMethod(proxy, method, args);
        }

        // extension
        if ("filter".equals(method.getName())) {
            return invokeFilterMethod(proxy, method, args);
        }

        // extension
        if ("should".equals(method.getName())) {
            return invokeShouldMethod(proxy, method, args);
        }

        // extension
        if ("toString".equals(method.getName())) {
            return String.format("{name: %s, selector: %s}",
                    getContext().getName(),
                    getContext().getSelector()
            );
        }

        Class<?> proxyClass = method.getReturnType();

        String name = ReflectionUtils.getName(method, args);
        String selector = ReflectionUtils.getSelector(method, args);

        Context childContext = getContext().newChildContext(name, selector, method.getReturnType());

        // html element proxy (recurse)
        if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
            return createProxy(method.getReturnType(), WebElement.class, childContext, () ->
                    ((SearchContext) proxy).findElement(By.xpath(selector))
            );
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

    @SuppressWarnings("unchecked")
    private Object invokeTargetMethod(Supplier<T> targetProvider, Method method, Object[] args)
            throws Throwable {
        return ((SlowLoadableComponent<Object>) () -> {
            if (List.class.isAssignableFrom(getTargetClass())) {
                Object target = targetProvider.get();
                if (getContext().getStore().containsKey("filter")) {
                    List<Matcher> matchers = (List<Matcher>) getContext().getStore().get("filter");
                    target = ((List) target).stream()
                            .filter(element -> matchers.stream().allMatch(matcher -> matcher.matches(element)))
                            .collect(Collectors.toList());
                }
                return method.invoke(target, args);
            } else {
                Object target = targetProvider.get();
                return method.invoke(target, args);
            }
        }).get();
    }

    @SuppressWarnings({"unchecked", "unused"})
    private Object invokeShouldMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher matcher = (Matcher) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            assertThat(proxy, matcher);
            return proxy;
        }).get();
    }

    @SuppressWarnings({"unchecked", "unused"})
    private Object invokeWaitUntilMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher matcher = (Matcher) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            if (matcher.matches(proxy)) {
                return proxy;
            }
            throw new NoSuchElementException("No such element exception");
        }).get();
    }

    @SuppressWarnings({"unchecked", "unused"})
    private Object invokeFilterMethod(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, Object> store = getContext().getStore();
        List<Matcher> matchers = store.containsKey("filter") ?
                (List<Matcher>) store.get("filter") : new ArrayList<>();
        matchers.add((Matcher) args[0]);
        store.put("filter", matchers);
        return proxy;
    }

    private <R> Object createProxy(Class<?> proxyClass, Class<R> targetClass,
                                   Context context, Supplier<R> supplier) {
        return Proxies.simpleProxy(
                proxyClass, new WebBlockMethodHandler<>(context, targetClass, supplier)
        );
    }

}
