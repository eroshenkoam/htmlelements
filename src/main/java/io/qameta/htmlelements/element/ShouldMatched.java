package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

interface ShouldMatched <T, R> {

    R should (Matcher<T> matcher);
}
