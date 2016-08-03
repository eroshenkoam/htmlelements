package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

public interface ShouldMatched <T, R> {

    R should (Matcher<T> matcher);
}
