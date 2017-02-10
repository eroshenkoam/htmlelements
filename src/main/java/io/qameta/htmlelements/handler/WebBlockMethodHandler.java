package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.MethodInvocationException;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.extension.Retry;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.waiter.Waiter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethodsNames;

public class WebBlockMethodHandler implements InvocationHandler {

    private final Supplier targetProvider;

    private final Context context;

    private final Class[] targetClasses;

    public WebBlockMethodHandler(Context context, Supplier targetProvider, Class... targetClasses) {
        this.targetProvider = targetProvider;
        this.targetClasses = targetClasses;
        this.context = context;
    }

    private Class[] getTargetClasses() {
        return targetClasses;
    }

    private Supplier getTargetProvider() {
        return this.targetProvider;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class[] targetClass = getTargetClasses();

        int timeout = 0;
        int pooling = 0;

        if (method.isAnnotationPresent(Retry.class)) {
            Retry retry = method.getAnnotation(Retry.class);
            timeout = retry.timeout();
            pooling = retry.pooling();
        }

        // web element proxy
        if (getMethodsNames(targetClass, "equals", "hashCode").contains(method.getName())) {
            return invokeTargetMethod(timeout, pooling, getTargetProvider(), method, args);
        }

        MethodHandler<Object> handler = getContext().getRegistry().getHandler(method)
                .orElseThrow(() -> new NotImplementedException(method));

        return ((Waiter<Object>) () -> handler.handle(getContext(), proxy, method, args))
                .get(timeout, pooling);
    }

    @SuppressWarnings("unchecked")
    private Object invokeTargetMethod(int timeout, int pooling, Supplier targetProvider, Method method, Object[] args)
            throws Throwable {
        try {
            return ((Waiter<Object>) () -> safeInvokeTargetMethod(targetProvider, method, args))
                    .get(timeout, pooling);
        } catch (MethodInvocationException e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    private Object safeInvokeTargetMethod(Supplier targetProvider, Method method, Object[] args) {
        Object target = targetProvider.get();
        for (TargetModifier<Object> modifier : getContext().getRegistry().getExtensions(TargetModifier.class)) {
            target = modifier.modify(getContext(), target);
        }
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MethodInvocationException(e);
        }
    }

    public Object getUnwrappedObject() {
        return targetProvider.get();
    }
}
