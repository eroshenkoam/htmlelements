package io.qameta.htmlelements.statement;

import com.google.common.base.Throwables;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * eroshenkoam
 * 25.02.17
 */
public class RetryStatement implements StatementWrapper {

    private Duration timeout;

    private Duration polling;

    private List<Class<? extends Throwable>> ignoring;

    public RetryStatement() {
        this.timeout = new Duration(5, TimeUnit.SECONDS);
        this.polling = new Duration(250, TimeUnit.MILLISECONDS);
        this.ignoring = new ArrayList<>();
    }

    public RetryStatement withTimeout(long time, TimeUnit unit) {
        this.timeout = new Duration(time, unit);
        return this;
    }

    public RetryStatement pollingEvery(long time, TimeUnit unit) {
        this.polling = new Duration(time, unit);
        return this;
    }

    @SafeVarargs
    public final RetryStatement ignoring(Class<? extends Throwable>... exceptionType) {
        this.ignoring.addAll(Arrays.asList(exceptionType));
        return this;
    }

    @Override
    public Statement apply(Statement statement) throws Throwable {
        return () -> {
            Clock clock = new SystemClock();
            long end = clock.laterBy(timeout.in(TimeUnit.MILLISECONDS));
            Throwable lastException;
            do {
                try {
                    return statement.evaluate();
                } catch (Throwable e) {
                    lastException = e;
                    if (ignoring.stream().anyMatch(clazz -> clazz.isInstance(e))) {
                        try {
                            Thread.sleep(polling.in(TimeUnit.MILLISECONDS));
                        } catch (InterruptedException i) {
                            break;
                        }
                    } else {
                        Throwables.propagate(e);
                    }
                }
            } while ((clock.isNowBefore(end)));
            throw lastException;
        };
    }

}