package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.water.SlowLoadableComponent;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;

import java.lang.reflect.Method;
import java.util.Arrays;

class WaitUntilMatchedMethodCallHandler implements MethodCallHandler {

    @Override
    public boolean canHandle(Method method) {
        return "waitUntil".equals(method.getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher[] matcherList = (Matcher[]) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            if (Arrays.stream(matcherList).allMatch(matcher -> matcher.matches(proxy))) {
                return proxy;
            }
            throw new NoSuchElementException("No such element exception");
        }).get();
    }

}
