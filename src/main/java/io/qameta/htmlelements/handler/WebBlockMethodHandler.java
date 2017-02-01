package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.MethodInvocationException;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.waiter.SlowLoadableComponent;

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

        // web element proxy
        if (getMethodsNames(targetClass, "equals", "hashCode").contains(method.getName())) {
            return invokeTargetMethod(getTargetProvider(), method, args);
        }

        return getContext().getRegistry().getHandler(method)
                .orElseThrow(() -> new NotImplementedException(method))
                .handle(getContext(), proxy, method, args);
    }

    @SuppressWarnings("unchecked")
    private Object invokeTargetMethod(Supplier targetProvider, Method method, Object[] args)
            throws Throwable {
        try {
            return ((SlowLoadableComponent<Object>) () -> safeInvokeTargetMethod(targetProvider, method, args)).get();
        } catch (MethodInvocationException e){
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
