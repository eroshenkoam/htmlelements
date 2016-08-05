package io.qameta.htmlelements.handler;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;

class ShouldMatchedMethodCallHandler implements ByNameMethodCallHandler {

    @Override
    public String getHandleMethodName() {
        return "should";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(5));

        Throwable lasException = null;
        while (clock.isNowBefore(end)) {
            try {
                Arrays.stream((Matcher[]) args[0]).forEach(matcher -> assertThat(proxy, matcher));
                return proxy;
            } catch (Throwable e) {
                lasException = e;
            } finally {
                Thread.sleep(250);
            }
        }
        throw lasException;
    }
}
