package io.qameta.htmlelements.water;

import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.concurrent.TimeUnit;

@FunctionalInterface
public interface SlowLoadableComponent<T> {

    default T get() throws Throwable {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(5));

        do {
            try {
                return getSafely();
            } catch (Throwable e) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException i) {
                    break;
                }
            }
        } while ((clock.isNowBefore(end)));

        return getSafely();
    }

    T getSafely() throws Throwable;
}
