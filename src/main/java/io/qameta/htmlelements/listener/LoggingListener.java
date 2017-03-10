package io.qameta.htmlelements.listener;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.context.Store;
import io.qameta.htmlelements.extension.MethodHandler;
import io.qameta.htmlelements.handler.WebBlockMethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class LoggingListener implements WebBlockListener {

    private final Logger log = LoggerFactory.getLogger(WebBlockMethodHandler.class);

    public void beforeMethodHandlerInvocation(Context context, Method method, Object[] args, MethodHandler handler){
        log.info("Invoking method «{}» with args «{}» handled by «{}»", method.getName(), args,
                handler.getClass().getDeclaringClass().getName());
    }

    public void beforeWebElementMethodInvocation(Context context, Method method, Object[] args){
        final String description = (String) context.getStore().get(Store.DESCRIPTION_KEY).orElse("without description");
        log.info("Invoking WebElement's method «{}» with args «{}» for web element «{}»", method.getName(), args,
                description);
    }

    @Override
    public void afterMethodInvocation(Method method, Object result) {
        log.info("Got «{}» result after method «{}» was successfully invoked", result, method.getName());
    }
}
