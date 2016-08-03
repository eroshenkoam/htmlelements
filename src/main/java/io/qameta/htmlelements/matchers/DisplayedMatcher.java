package io.qameta.htmlelements.matchers;

import org.hamcrest.*;
import org.openqa.selenium.*;

public class DisplayedMatcher extends TypeSafeMatcher<WebElement> {
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