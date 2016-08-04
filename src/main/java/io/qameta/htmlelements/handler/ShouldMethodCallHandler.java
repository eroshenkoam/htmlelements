package io.qameta.htmlelements.handler;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;

class ShouldMethodCallHandler implements ByNameMethodCallHandler {

    @Override
    public String getHandleMethodName() {
        return "should";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(5000));

        Throwable lasException = null;
        while (clock.isNowBefore(end)) {
            try {
                Matcher<WebElement> matcher = (Matcher<WebElement>) args[0];
                assertThat((WebElement) proxy, matcher);
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
