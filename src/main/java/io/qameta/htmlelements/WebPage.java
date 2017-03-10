package io.qameta.htmlelements;

import io.qameta.htmlelements.extension.DriverProvider;
import io.qameta.htmlelements.extension.Retry;
import io.qameta.htmlelements.extension.page.AddListenerMethod;
import io.qameta.htmlelements.extension.page.GoMethod;
import io.qameta.htmlelements.extension.page.IsAtMethod;
import io.qameta.htmlelements.listener.WebBlockListener;
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

    @Retry
    @IsAtMethod
    void isAt(Matcher<String> url);

    @AddListenerMethod
    void addListener(WebBlockListener listener);

    default void open(String url) {
        getWrappedDriver().get(url);
    }

}
