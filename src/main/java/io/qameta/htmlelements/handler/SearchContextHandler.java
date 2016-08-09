package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.proxy.Proxies;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethods;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchContextHandler<T> implements InvocationHandler {

    private final Supplier<T> targetProvider;

    private final Class<T> tartetClass;

    public SearchContextHandler(Class<T> targetClass, Supplier<T> targetProvider) {
        this.targetProvider = targetProvider;
        this.tartetClass = targetClass;
    }

    public Class<T> getTargetClass() {
        return tartetClass;
    }

    private Supplier<T> getTargetProvider() {
        return this.targetProvider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<T> targetClass = getTargetClass();

        if (getMethods(targetClass).contains(method)) {
            return invokeTargetMethod(getTargetProvider(), method, args);
        }

        if ("should".equals(method.getName())) {
            return invokeShouldMethod(proxy, args);
        }

        Class<?> proxyClass = method.getReturnType();

        if (method.isAnnotationPresent(FindBy.class) && WebElement.class.isAssignableFrom(proxyClass)) {
            String value = method.getAnnotation(FindBy.class).value();
            return createProxy(method.getReturnType(), WebElement.class, () ->
                    ((SearchContext) proxy).findElement(By.xpath(value)));
        }
        if (method.isAnnotationPresent(FindBy.class) && List.class.isAssignableFrom(method.getReturnType())) {
            String value = method.getAnnotation(FindBy.class).value();
            return createProxy(method.getReturnType(), List.class, () -> {
                List<WebElement> originalElements = ((SearchContext) proxy).findElements(By.xpath(value));
                Type methodReturnType = ((ParameterizedType) method
                        .getGenericReturnType()).getActualTypeArguments()[0];
                return (List) originalElements.stream()
                        .map(element -> createProxy((Class<?>) methodReturnType, WebElement.class, () -> element))
                        .collect(toList());
            });
        }

        return new NotImplementedException(method);
    }

    private Object invokeTargetMethod(Supplier<T> targetProvider, Method method, Object[] args) throws Throwable {
        //здесь нужно пытаться выполнить метод несколько раз
        T target = targetProvider.get();
        return method.invoke(target, args);
    }

    private Object invokeShouldMethod(Object proxy, Object[] args) {
        Matcher[] matcherList = (Matcher[]) args[0];
        Arrays.stream(matcherList).forEach(matcher -> {
            assertThat(proxy, matcher);
        });
        return proxy;
    }

    private <R> Object createProxy(Class<?> proxyClass, Class<R> targetClass, Supplier<R> supplier) {
        return Proxies.simpleProxy(
                proxyClass,
                new SearchContextHandler<>(targetClass, supplier)
        );
    }

}
