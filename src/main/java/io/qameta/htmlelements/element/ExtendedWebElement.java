package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.elements.HoverMethod;
import io.qameta.htmlelements.extension.elements.JsClickMethod;
import io.qameta.htmlelements.extension.elements.ShouldMethod;
import io.qameta.htmlelements.extension.Timeout;
import io.qameta.htmlelements.extension.elements.ToStringMethod;
import io.qameta.htmlelements.extension.elements.UnwrappedWebElement;
import io.qameta.htmlelements.extension.elements.WaitUntilMethod;
import io.qameta.htmlelements.extension.elements.ScrollMethod;
import io.qameta.htmlelements.matcher.PredicateMatcher;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @HoverMethod
    FluentType hover();

    @JsClickMethod
    void jsClick(); // Workaround for click in internet explorer 11

    @ScrollMethod
    FluentType scrollToElement(); // Workaround while geckodriver cannot scroll with hover

    @UnwrappedWebElement
    WebElement getUnwrappedWebElement();

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
