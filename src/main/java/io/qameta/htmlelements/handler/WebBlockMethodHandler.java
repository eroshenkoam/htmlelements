package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethodsNames;

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

}
