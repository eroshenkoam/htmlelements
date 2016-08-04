package io.qameta.htmlelements.matchers;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class HasTextMatcher extends TypeSafeMatcher<WebElement> {
    private final Matcher<String> textMatcher;

    private HasTextMatcher(Matcher<String> textMatcher) {
        this.textMatcher = textMatcher;
    }

    @Override
    public boolean matchesSafely(WebElement item) {
        return textMatcher.matches(item.getText());
    }

    public void describeTo(Description description) {
        description.appendText("element text ").appendDescriptionOf(textMatcher);
    }

    @Override
    protected void describeMismatchSafely(WebElement item, Description mismatchDescription) {
        mismatchDescription.
                appendText("text of element ").
                appendValue(item).
                appendText(" was ").
                appendValue(item.getText());
    }

    @Factory
    public static Matcher<WebElement> hasText(final Matcher<String> textMatcher) {
        return new HasTextMatcher(textMatcher);
    }

    @Factory
    public static Matcher<WebElement> hasText(final String text) {
        return new HasTextMatcher(is(text));
    }
}