package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.HoverMethod;
import io.qameta.htmlelements.extension.ShouldMethod;
import io.qameta.htmlelements.extension.Timeout;
import io.qameta.htmlelements.extension.ToStringMethod;
import io.qameta.htmlelements.extension.WaitUntilMethod;
import io.qameta.htmlelements.matcher.PredicateMatcher;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;

import java.util.function.Predicate;

public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @HoverMethod
    FluentType hover();

    @WaitUntilMethod
    FluentType waitUntil(String message, Matcher matcher);

    @WaitUntilMethod
    FluentType waitUntil(String message, Matcher matcher, @Timeout long timeout);

    default FluentType waitUntil(Matcher matcher) {
        return waitUntil("", matcher);
    }

    default FluentType waitUntil(Predicate<FluentType> predicate) {
        return waitUntil("", predicate);
    }

    default FluentType waitUntil(String description, Predicate<FluentType> predicate) {
        return waitUntil(description, new PredicateMatcher<>(predicate));
    }

    @ShouldMethod
    FluentType should(String message, Matcher matcher, @Timeout long timeout);

    @ShouldMethod
    FluentType should(String message, Matcher matcher);

    default FluentType should(Matcher matcher) {
        return should("", matcher);
    }

    @ToStringMethod
    String toString();

}
