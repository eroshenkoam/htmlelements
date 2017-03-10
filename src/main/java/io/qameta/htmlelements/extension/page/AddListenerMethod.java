package io.qameta.htmlelements.extension.page;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.listener.ListenerManager;
import io.qameta.htmlelements.listener.WebBlockListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static io.qameta.htmlelements.context.Store.LISTENER_MANAGER_KEY;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(GoMethod.Extension.class)
public @interface AddListenerMethod {

    class Extension implements MethodHandler {

        @Override
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            ListenerManager listenerManager = (ListenerManager) context.getStore().get(LISTENER_MANAGER_KEY)
                    .orElseThrow(() -> new WebPageException("Couldn't add listener, ListenerManager is absent"));
            listenerManager.addListener((WebBlockListener) args[0]);
            return proxy;
        }
    }
}
