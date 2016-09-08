package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.HoverMethod;
import io.qameta.htmlelements.extension.SelectorProvider;
import io.qameta.htmlelements.extension.ShouldMethod;
import io.qameta.htmlelements.extension.WaitUntilMethod;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.util.function.Predicate;

public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @SelectorProvider
    String getSelector();

    @DescriptionProvider
    String getDescription();

    @WaitUntilMethod
    FluentType waitUntil(Predicate<FluentType> predicate);

    @ShouldMethod
    FluentType should(Matcher matcher);

    @HoverMethod
    FluentType hover();

    default FluentType waitUntil(String description, Predicate<FluentType> predicate) {
        return waitUntil(predicate);
    }

    default FluentType waitUntil(Matcher matcher) {
        return waitUntil(matcher.toString(), matcher::matches);
    }

}
