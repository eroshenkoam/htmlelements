package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;
import java.util.*;

/**
 * eroshenkoam
 * 22.03.17
 */
public class ListenerStatement implements StatementWrapper {

    private final String description;

    private final Method method;

    private final Object[] args;

    private List<Listener> listeners;

    public ListenerStatement(String description, Method method, Object[] args) {
        this.listeners = new ArrayList<>();
        this.description = description;
        this.method = method;
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
                listeners.forEach(listener -> listener.beforeMethodCall(description, method, args));
                return statement.evaluate();
            } catch (Throwable exception) {
                listeners.forEach(listener -> listener.onMethodFailed(description, method, exception));
                throw exception;
            } finally {
                listeners.forEach(listener -> listener.afterMethodCall(description, method));
            }
        };
    }

}
