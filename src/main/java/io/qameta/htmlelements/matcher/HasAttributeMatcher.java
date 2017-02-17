package io.qameta.htmlelements.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class HasAttributeMatcher extends TypeSafeMatcher<WebElement> {
    private final Matcher<String> textMatcher;
    private final String attribute;

    private HasAttributeMatcher(String attribute, Matcher<String> textMatcher) {
        this.attribute = attribute;
        this.textMatcher = textMatcher;
    }

    @Override
    public boolean matchesSafely(WebElement item) {
        return textMatcher.matches(item.getAttribute(attribute));
    }

    public void describeTo(Description description) {
        description.appendText("element attribute ").appendValue(attribute).appendText(" ").appendDescriptionOf(textMatcher);
    }

    @Override
    protected void describeMismatchSafely(WebElement item, Description mismatchDescription) {
        mismatchDescription.
                appendText("attribute ").
                appendValue(attribute).
                appendText(" of element ").
                appendValue(item).
                appendText(" was ").
                appendValue(item.getAttribute(attribute));
    }

    @Factory
    public static Matcher<WebElement> hasAttribute(final String attribute, final Matcher<String> textMatcher) {
        return new HasAttributeMatcher(attribute, textMatcher);
    }

    @Factory
    public static Matcher<WebElement> hasClass(final Matcher<String> textMatcher) {
        return hasAttribute("class", textMatcher);
    }

    @Factory
    public static Matcher<WebElement> hasClass(final String text) {
        return hasAttribute("class", is(text));
    }
}
