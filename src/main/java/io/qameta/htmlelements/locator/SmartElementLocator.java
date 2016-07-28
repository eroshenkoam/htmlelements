package io.qameta.htmlelements.locator;

import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.util.List;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class SmartElementLocator implements ElementLocator {

    public static final int DEFAULT_TIMEOUT = 5;

    private final long timeOutInSeconds;

    private final Clock clock;

    private final SearchContext searchContext;

    private final Annotations annotations;

    public SmartElementLocator(SearchContext searchContext, Annotations annotations) {
        this.searchContext = searchContext;
        this.annotations = annotations;
        this.clock = new SystemClock();
        this.timeOutInSeconds = Integer.getInteger("webdriver.timeouts.implicitlywait", DEFAULT_TIMEOUT);
    }

    protected SearchContext getSearchContext() {
        return searchContext;
    }

    protected Annotations getAnnotations() {
        return annotations;
    }

    @Override
    public WebElement findElement() {
        long end = this.clock.laterBy(TimeUnit.SECONDS.toMillis(this.timeOutInSeconds));

        while (this.clock.isNowBefore(end)) {
            try {
                By by = getAnnotations().buildBy();
                WebElement element = getSearchContext().findElement(by);
                if (isValid(element)) {
                    return element;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                this.waitFor();
            }
        }

        throw new NoSuchElementException("No such element exception");
    }

    @Override
    public List<WebElement> findElements() {
        By by = getAnnotations().buildBy();
        return getSearchContext().findElements(by);
    }

    protected long sleepFor() {
        return 500L;
    }

    private void waitFor() {
        try {
            Thread.sleep(this.sleepFor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid(WebElement element) {
        return getAnnotations().getValidators().parallelStream()
                .allMatch(validator -> validator.isValid(element));
    }

}
