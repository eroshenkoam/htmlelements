package io.qameta.htmlelements.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class HasClassMatcher extends TypeSafeMatcher<WebElement> {
    private final Matcher<String> textMatcher;

    private HasClassMatcher(Matcher<String> textMatcher) {
        this.textMatcher = textMatcher;
    }

    @Override
    public boolean matchesSafely(WebElement item) {
        return textMatcher.matches(item.getAttribute("class"));
    }

    public void describeTo(Description description) {
        description.appendText("element class ").appendDescriptionOf(textMatcher);
    }

    @Override
    protected void describeMismatchSafely(WebElement item, Description mismatchDescription) {
        mismatchDescription.
                appendText("class of element ").
                appendValue(item).
                appendText(" was ").
                appendValue(item.getAttribute("class"));
    }

    @Factory
    public static Matcher<WebElement> hasClass(final Matcher<String> textMatcher) {
        return new HasClassMatcher(textMatcher);
    }

    @Factory
    public static Matcher<WebElement> hasClass(final String text) {
        return new HasClassMatcher(is(text));
    }
}
