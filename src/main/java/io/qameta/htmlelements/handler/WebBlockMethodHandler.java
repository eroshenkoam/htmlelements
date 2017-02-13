package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.exception.MethodInvocationException;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.extension.Retry;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.listener.ListenerManager;
import io.qameta.htmlelements.waiter.WaitingStatement;
import org.openqa.selenium.StaleElementReferenceException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getMethodsNames;

public class WebBlockMethodHandler implements InvocationHandler {

    private final Supplier targetProvider;

    private final Context context;

    private final Class[] targetClasses;

    private final ListenerManager listener;

    public WebBlockMethodHandler(Context context, Supplier targetProvider, Class... targetClasses) {
        this.targetProvider = targetProvider;
        this.targetClasses = targetClasses;
        this.context = context;
        this.listener = (ListenerManager) context.getStore().get(Store.LISTENER_MANAGER_KEY).orElse(new ListenerManager());
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

        int timeout = 0;
        int polling = 0;
        List<Class<? extends Throwable>> ignoring = Arrays.asList(NoSuchElementException.class,
                StaleElementReferenceException.class);

        if (method.isAnnotationPresent(Retry.class)) {
            Retry retry = method.getAnnotation(Retry.class);
            timeout = retry.timeout();
            polling = retry.pooling();
        }

        WaitingStatement<Object> waiter = new WaitingStatement<>(
                () -> safeInvokeTargetMethod(proxy, getTargetProvider(), method, args), ignoring);

        Object result = waiter.evaluate(timeout, polling);
        listener.afterMethodInvocation(method, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private Object safeInvokeTargetMethod(Object proxy, Supplier targetProvider, Method method, Object[] args) {
        if (!isWebElementMethod(method)) {
            return invokeMethodHandler(proxy, method, args);
        }
        Object target = targetProvider.get();
        for (TargetModifier<Object> modifier : getContext().getRegistry().getExtensions(TargetModifier.class)) {
            target = modifier.modify(getContext(), target);
        }
        try {
            listener.beforeWebElementMethodInvocation(getContext(), method, args);
            return method.invoke(target, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MethodInvocationException(e);
        }
    }

    private Object invokeMethodHandler(Object proxy, Method method, Object[] args) {
        MethodHandler<Object> handler = getContext().getRegistry().getHandler(method)
                .orElseThrow(() -> new NotImplementedException(method));
        try {
            listener.beforeMethodHandlerInvocation(getContext(), method, args, handler);
            return handler.handle(getContext(), proxy, method, args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isWebElementMethod(Method method) {
        Class[] targetClass = getTargetClasses();
        return getMethodsNames(targetClass, "equals", "hashCode").contains(method.getName());
    }

    public Object getUnwrappedObject() {
        return targetProvider.get();
    }
}
