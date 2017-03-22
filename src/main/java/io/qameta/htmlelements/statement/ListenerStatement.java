package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * eroshenkoam
 * 22.03.17
 */
public class ListenerStatement implements StatementWrapper {

    private final Method method;

    private final Object[] args;

    private List<Listener> listeners;

    public ListenerStatement(Method method, Object[] args) {
        this.listeners = new ArrayList<>();
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
                listeners.forEach(listener -> listener.beforeMethodCall(method, args));
                return statement.evaluate();
            } catch (Throwable e) {
                listeners.forEach(listener -> listener.onMethodFailed(method, args));
                throw e;
            } finally {
                listeners.forEach(listener -> listener.afterMethodCall(method, args));
            }
        };
    }

}
