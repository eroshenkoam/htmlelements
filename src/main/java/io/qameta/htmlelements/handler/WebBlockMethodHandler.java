package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.exception.MethodInvocationException;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.waiter.WaitingStatement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
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
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class[] targetClass = getTargetClasses();

        Store store = getContext().getStore();
        Long timeout = (Long) store.get("timeout").orElse(0);
        List<Class<? extends Throwable>> ignoring = (List<Class<? extends Throwable>>) store
                .get("ignoring").orElse(Collections.emptyList());

        // web element proxy
        if (getMethodsNames(targetClass, "equals", "hashCode").contains(method.getName())) {
            return new WaitingStatement(
                    () -> safeInvokeTargetMethod(getTargetProvider(), method, args), timeout, ignoring
            ).evaluate();
        }

        return getContext().getRegistry().getHandler(method)
                .orElseThrow(() -> new NotImplementedException(method))
                .handle(getContext(), proxy, method, args);
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

}
