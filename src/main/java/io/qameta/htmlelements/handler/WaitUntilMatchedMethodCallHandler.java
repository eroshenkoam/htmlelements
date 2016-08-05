package io.qameta.htmlelements.handler;

import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class WaitUntilMatchedMethodCallHandler implements ByNameMethodCallHandler {

    @Override
    public String getHandleMethodName() {
        return "waitUntil";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(5));

        Throwable lasException = null;
        while (clock.isNowBefore(end)) {
            try {
                if (Arrays.stream((Matcher[]) args[0]).allMatch(matcher -> matcher.matches(proxy))) {
                    return proxy;
                } else {
                    throw new NoSuchElementException("No such element exception");
                }
            } catch (Throwable e) {
                lasException = e;
            } finally {
                Thread.sleep(250);
            }
        }
        throw lasException;

    }

}
