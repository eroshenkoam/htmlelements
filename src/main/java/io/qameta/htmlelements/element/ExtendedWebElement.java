package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.HoverMethod;
import io.qameta.htmlelements.extension.Retry;
import io.qameta.htmlelements.extension.ShouldMethod;
import io.qameta.htmlelements.extension.ToStringMethod;
import io.qameta.htmlelements.extension.WaitUntilMethod;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.util.function.Predicate;

public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @Retry
    @WaitUntilMethod
    FluentType waitUntil(String description, Predicate<FluentType> predicate);

    @Retry
    @ShouldMethod
    FluentType should(String message, Matcher matcher);

    @Retry
    @HoverMethod
    FluentType hover();

    @ToStringMethod
    String toString();

    default FluentType waitUntil(Predicate<FluentType> predicate) {
        return waitUntil("", predicate);
    }

    default FluentType waitUntil(Matcher matcher) {
        return waitUntil(matcher.toString(), matcher::matches);
    }

    default FluentType waitUntil(String message, Matcher matcher) {
        return waitUntil(message, matcher::matches);
    }

    default FluentType should(Matcher matcher) {
        return should("", matcher);
    }

}
