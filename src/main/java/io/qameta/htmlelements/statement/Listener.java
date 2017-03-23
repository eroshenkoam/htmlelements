package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;

/**
 * eroshenkoam
 * 22.03.17
 */
public interface Listener {

    default void beforeMethodCall(Object proxy, Method method, Object[] args) {
    }

    default void onMethodFailed(Object proxy, Method method, Throwable exception) {
    }

    default void afterMethodCall(Object proxy, Method method) {
    }

}
