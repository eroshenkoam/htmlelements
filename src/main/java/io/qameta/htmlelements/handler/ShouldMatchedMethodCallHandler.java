package io.qameta.htmlelements.handler;

import io.qameta.htmlelements.water.SlowLoadableComponent;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

class ShouldMatchedMethodCallHandler implements MethodCallHandler {

    @Override
    public boolean canHandle(Method method) {
        return "should".equals(method.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher[] matcherList = (Matcher[]) args[0];
        return ((SlowLoadableComponent<Object>) () -> {
            Arrays.stream(matcherList).forEach(matcher -> {
                assertThat(proxy, matcher);
            });
            return proxy;
        }).get();
    }
}
