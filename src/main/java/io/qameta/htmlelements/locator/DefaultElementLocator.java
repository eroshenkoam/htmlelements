package io.qameta.htmlelements.locator;

import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

public class DefaultElementLocator implements ElementLocator {

    private final SearchContext searchContext;

    private final Annotations annotations;

    public DefaultElementLocator(SearchContext searchContext, Annotations annotations) {
        this.searchContext = searchContext;
        this.annotations = annotations;
    }

    protected SearchContext getSearchContext() {
        return searchContext;
    }

    protected Annotations getAnnotations() {
        return annotations;
    }

    @Override
    public WebElement findElement() {
        By by = getAnnotations().buildBy();
        return getSearchContext().findElement(by);
    }

    @Override
    public List<WebElement> findElements() {
        By by = getAnnotations().buildBy();
        return getSearchContext().findElements(by);
    }
}
