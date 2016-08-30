package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.DriverProvider;
import io.qameta.htmlelements.extension.SelectorProvider;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import java.util.function.Predicate;

public interface ExtendedWebElement<FluentType> extends WebElement, Locatable {

    @DriverProvider
    WebDriver getDriver();

    @SelectorProvider
    String getSelector();

    @DescriptionProvider
    String getDescription();

    default FluentType waitUntil(String description, Predicate<FluentType> predicate) {
        return waitUntil(predicate);
    }

    default FluentType waitUntil(Matcher matcher) {
        return waitUntil(matcher.toString(), matcher::matches);
    }

    FluentType waitUntil(Predicate<FluentType> predicate);

    FluentType should(Matcher matcher);

    @SuppressWarnings("unchecked")
    default FluentType hover() {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(this).perform();
        return (FluentType) this;
    }

}
