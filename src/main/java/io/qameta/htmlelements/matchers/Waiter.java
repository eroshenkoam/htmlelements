package io.qameta.htmlelements.matchers;

import org.hamcrest.SelfDescribing;

import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class Waiter implements SelfDescribing {
    private static final long DEFAULT_POLLING_INTERVAL = SECONDS.toMillis(1);

    private long pollingInterval;
    private long startTime;

    protected Waiter() {
        pollingInterval = DEFAULT_POLLING_INTERVAL;
    }

    public void startWaiting() {
        startTime = System.currentTimeMillis();
    }

    public abstract boolean shouldStopWaiting();

    public long getPollingInterval() {
        return pollingInterval;
    }

    public Waiter withPollingInterval(long pollingInterval) {
        setPollingInterval(pollingInterval);
        return this;
    }

    protected long getStartTime() {
        return startTime;
    }

    protected void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }
}