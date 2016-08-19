package io.qameta.htmlelements;

import io.qameta.htmlelements.element.HasContext;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface WebPage extends HasContext, WrapsDriver, SearchContext {

    @Override
    default WebDriver getWrappedDriver() {
        return getContext().getDriver();
    }

    default void open(String url) {
        getWrappedDriver().get(url);
    }

}
