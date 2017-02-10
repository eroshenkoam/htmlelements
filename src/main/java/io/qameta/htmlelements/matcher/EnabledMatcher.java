package io.qameta.htmlelements.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class EnabledMatcher extends TypeSafeMatcher<WebElement> {

    private EnabledMatcher() {
    }

    @Override
    protected boolean matchesSafely(WebElement element) {
        try {
            return element.isEnabled();
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("element is enabled");
    }

    @Override
    public void describeMismatchSafely(WebElement element, Description mismatchDescription) {
        mismatchDescription.appendText("element ").appendValue(element).appendText(" is not enabled");
    }

    @Factory
    public static Matcher<WebElement> enabled() {
        return new EnabledMatcher();
    }
}

