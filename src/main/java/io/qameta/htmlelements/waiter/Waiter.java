package io.qameta.htmlelements.waiter;

import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.concurrent.TimeUnit;

@FunctionalInterface
public interface Waiter<T> {

    default T get(int timeout, int pooling) throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(timeout));
        do {
            try {
                return getSafely();
            } catch (Throwable e) {
                try {
                    Thread.sleep(pooling);
                } catch (InterruptedException i) {
                    break;
                }
            }
        } while ((clock.isNowBefore(end)));
        return getSafely();
    }

    T getSafely() throws Throwable;
}
