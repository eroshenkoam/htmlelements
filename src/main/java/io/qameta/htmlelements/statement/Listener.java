package io.qameta.htmlelements.statement;

import java.lang.reflect.Method;

/**
 * eroshenkoam
 * 22.03.17
 */
public interface Listener {

    default void beforeMethodCall(Method method, Object[] args) {
    }

    default void onMethodFailed(Method method, Object[] args){
    }

    default void afterMethodCall(Method method, Object[] args) {
    }

}
