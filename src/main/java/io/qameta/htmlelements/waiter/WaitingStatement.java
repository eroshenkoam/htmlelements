package io.qameta.htmlelements.waiter;

import com.google.common.base.Throwables;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author ehborisov
 */
public class WaitingStatement<T> {

    private long timeout;
    private List<Class<? extends Throwable>> ignoring;
    private Supplier<Object> supplier;

    public WaitingStatement(Supplier<Object> supplier, long timeout, List<Class<? extends Throwable>> ignoring) {
        this.timeout = timeout;
        this.supplier = supplier;
        this.ignoring = ignoring;
    }

    public Object evaluate() {
        Clock clock = new SystemClock();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(timeout));
        do {
            try {
                return supplier.get();
            } catch (Throwable e) {
                if(ignoring.stream().anyMatch(clazz -> clazz.isInstance(e))){
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException i) {
                        break;
                    }
                } else {
                    Throwables.propagate(e);
                }
            }
        } while ((clock.isNowBefore(end)));
        return supplier.get();
    }
}
