package io.qameta.htmlelements;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface WebPage extends WrapsDriver, SearchContext {

    default void open(String url) {
        getWrappedDriver().get(url);
    }

}
