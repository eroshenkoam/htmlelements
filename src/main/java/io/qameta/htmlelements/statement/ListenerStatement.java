package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;
import java.util.*;

/**
 * eroshenkoam
 * 22.03.17
 */
public class ListenerStatement implements StatementWrapper {

    private final Object proxy;

    private final Method method;

    private final Object[] args;

    private List<Listener> listeners;

    public ListenerStatement(Object proxy, Method method, Object[] args) {
        this.listeners = new ArrayList<>();
        this.method = method;
        this.proxy = proxy;
        this.args = args;
    }

    public ListenerStatement withListeners(List<Listener> listener) {
        listeners.addAll(listener);
        return this;
    }

    @Override
    public Statement apply(Statement statement) throws Throwable {
        return () -> {
            try {
                listeners.forEach(listener -> listener.beforeMethodCall(proxy, method, args));
                return statement.evaluate();
            } catch (Throwable exception) {
                listeners.forEach(listener -> listener.onMethodFailed(proxy, method, exception));
                throw exception;
            } finally {
                listeners.forEach(listener -> listener.afterMethodCall(proxy, method));
            }
        };
    }

}
