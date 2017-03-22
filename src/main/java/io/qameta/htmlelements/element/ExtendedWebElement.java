package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.HoverMethod;
import io.qameta.htmlelements.extension.ShouldMethod;
import io.qameta.htmlelements.extension.Timeout;
import io.qameta.htmlelements.extension.ToStringMethod;
import io.qameta.htmlelements.extension.WaitUntilMethod;
import io.qameta.htmlelements.matcher.PredicateMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.util.function.Predicate;

public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @WaitUntilMethod
    FluentType waitUntil(String message, Matcher matcher, @Timeout long timeout);

    @WaitUntilMethod
    FluentType waitUntil(String message, Matcher matcher);

    @ShouldMethod
    FluentType should(String message, Matcher matcher, @Timeout long timeout);

    @ShouldMethod
    FluentType should(String message, Matcher matcher);

    @HoverMethod
    FluentType hover();

    @ToStringMethod
    String toString();

    default FluentType waitUntil(Predicate<FluentType> predicate) {
        return waitUntil("", predicate);
    }

    default FluentType waitUntil(Matcher matcher) {
        return waitUntil("", matcher);
    }

    default FluentType waitUntil(String description, Predicate<FluentType> predicate) {
        return waitUntil(description, new PredicateMatcher<>(predicate));
    }

    default FluentType should(Matcher matcher) {
        return should("", matcher);
    }

}
