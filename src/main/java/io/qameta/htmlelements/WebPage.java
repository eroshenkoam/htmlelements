package io.qameta.htmlelements;

import io.qameta.htmlelements.extension.DriverProvider;
import io.qameta.htmlelements.extension.elements.ToStringMethod;
import io.qameta.htmlelements.extension.page.GoMethod;
import io.qameta.htmlelements.extension.page.IsAtMethod;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface WebPage extends WrapsDriver, SearchContext {

    @DriverProvider
    WebDriver getWrappedDriver();

    @GoMethod
    void go();

    @IsAtMethod
    void isAt(Matcher<String> url);

    default void open(String url) {
        getWrappedDriver().get(url);
    }

    @ToStringMethod
    String toString();

}
