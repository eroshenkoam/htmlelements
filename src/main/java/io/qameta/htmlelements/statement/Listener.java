package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;

/**
 * eroshenkoam
 * 22.03.17
 */
public interface Listener {

    default void beforeMethodCall(String description, Method method, Object[] args) {
    }

    default void onMethodFailed(String description, Method method, Throwable exception) {
    }

    default void afterMethodCall(String description, Method method) {
    }

}
