package io.qameta.htmlelements.handler;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class FilterMatchedMethodCallHandler implements ByNameMethodCallHandler {
    @Override
    public String getHandleMethodName() {
        return "filter";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Matcher[] matchers = (Matcher[]) args[0];
        ((List) proxy).removeIf(element -> !Arrays.stream(matchers).allMatch(matcher -> matcher.matches(element)));
        return proxy;
    }
}
