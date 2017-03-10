package io.qameta.htmlelements.listener;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.extension.MethodHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ListenerManager implements WebBlockListener {

    private List<WebBlockListener> listeners = new ArrayList<>();

    public void addListeners(Collection<WebBlockListener> listeners) {
        this.listeners.addAll(listeners);
    }

    public void addListener(WebBlockListener listener) {
        this.listeners.add(listener);
    }

    public void beforeMethodHandlerInvocation(Context context, Method method, Object[] args,
                                              MethodHandler handler) {
        listeners.forEach(l -> l.beforeMethodHandlerInvocation(context, method, args, handler));
    }

    public void beforeWebElementMethodInvocation(Context context, Method method, Object[] args) {
        listeners.forEach(l -> l.beforeWebElementMethodInvocation(context, method, args));
    }

    public void afterMethodInvocation(Method method, Object result) {
        listeners.forEach(l -> l.afterMethodInvocation(method, result));
    }
}
