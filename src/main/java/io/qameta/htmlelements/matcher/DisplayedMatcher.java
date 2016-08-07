package io.qameta.htmlelements.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class DisplayedMatcher extends TypeSafeMatcher<WebElement> {

    private DisplayedMatcher() {
    }

    @Override
    protected boolean matchesSafely(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("element is displayed on page");
    }

    @Override
    public void describeMismatchSafely(WebElement element, Description mismatchDescription) {
        mismatchDescription.appendText("element ").appendValue(element).appendText(" is not displayed on page");
    }

    /**
     * Creates matcher that checks if element is currently displayed on page.
     */
    @Factory
    public static Matcher<WebElement> displayed() {
        return new DisplayedMatcher();
    }
}