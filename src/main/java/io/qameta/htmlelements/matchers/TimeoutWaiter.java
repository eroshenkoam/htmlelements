package io.qameta.htmlelements.matchers;

import org.hamcrest.Description;

import java.util.concurrent.TimeUnit;

public class TimeoutWaiter extends Waiter {
    private static final long DEFAULT_TIMEOUT;
    private final long timeout;

    public static TimeoutWaiter timeoutHasExpired() {
        return new TimeoutWaiter();
    }

    public static TimeoutWaiter timeoutHasExpired(long timeoutInMilliseconds) {
        return new TimeoutWaiter(timeoutInMilliseconds);
    }

    public TimeoutWaiter() {
        this.timeout = DEFAULT_TIMEOUT;
    }

    public TimeoutWaiter(long timeout) {
        this.timeout = timeout;
    }

    public boolean shouldStopWaiting() {
        long currentTime = System.currentTimeMillis();
        return currentTime >= this.getStartTime() + this.timeout;
    }

    public void describeTo(Description description) {
        description.appendValue(Long.valueOf(this.timeout)).appendText(" milliseconds");
    }

    static {
        DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
    }
}
