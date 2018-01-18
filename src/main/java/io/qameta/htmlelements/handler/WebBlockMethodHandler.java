package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.MethodInvocationException;
import io.qameta.htmlelements.exception.NotImplementedException;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.extension.MethodParameters;
import io.qameta.htmlelements.extension.Retry;
import io.qameta.htmlelements.extension.TargetModifier;
import io.qameta.htmlelements.extension.Timeout;
import io.qameta.htmlelements.statement.ListenerStatement;
import io.qameta.htmlelements.statement.RetryStatement;
import io.qameta.htmlelements.statement.Statement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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

    @SuppressWarnings("unused") //
    private Supplier getTargetProvider() {
        return this.targetProvider;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class[] targetClass = getTargetClasses();

        Statement base;
        // web element proxy
        if (getMethodsNames(targetClass, "equals", "hashCode").contains(method.getName())) {
            base = () -> safeInvokeTargetMethod(targetProvider, method, args);
        } else {
            MethodHandler handler = getContext().getRegistry().getHandler(method)
                    .orElseThrow(() -> new NotImplementedException(method));
            base = () -> handler.handle(getContext(), proxy, method, args);
        }

        ListenerStatement statement = prepareListenerStatement(method, args);
        RetryStatement retry = prepareRetryStatement(method, args);

        try {
            return statement.apply(retry.apply(base)).evaluate();
        } catch (MethodInvocationException e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    private ListenerStatement prepareListenerStatement(Method method, Object[] args) {
        String description = getContext().getStore().get(Context.DESCRIPTION_KEY, String.class).orElse("");
        ListenerStatement statement = new ListenerStatement(description, method, args);
        getContext().getStore().get(Context.LISTENERS_KEY, List.class).ifPresent(statement::withListeners);
        return statement;
    }

    private RetryStatement prepareRetryStatement(Method method, Object[] args) {
        Properties properties = getContext().getStore().get(Context.PROPERTIES_KEY, Properties.class)
                .orElse(new Properties());
        RetryStatement retry = new RetryStatement(properties)
                .ignoring(Throwable.class);
        if (method.isAnnotationPresent(Retry.class)) {
            Retry retryAnnotation = method.getAnnotation(Retry.class);
            retry.withTimeout(retryAnnotation.timeoutInSeconds(), TimeUnit.SECONDS)
                    .pollingEvery(retryAnnotation.poolingInMillis(), TimeUnit.MILLISECONDS)
                    .ignoring(retryAnnotation.ignoring());
        }

        MethodParameters parameters = new MethodParameters(method, args);
        parameters.getParameter(Timeout.class, Long.class)
                .ifPresent(timeout -> retry.withTimeout(timeout, TimeUnit.SECONDS));

        return retry;
    }

    @SuppressWarnings("unchecked")
    private Object safeInvokeTargetMethod(Supplier targetProvider, Method method, Object[] args) throws Throwable {
        try {
            Object target = targetProvider.get();
            for (TargetModifier<Object> modifier : getContext().getRegistry().getExtensions(TargetModifier.class)) {
                target = modifier.modify(getContext(), target);
            }
            return method.invoke(target, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MethodInvocationException(e);
        }
    }

    public Object getUnwrappedObject() {
        return targetProvider.get();
    }
}
