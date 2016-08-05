package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

import java.util.function.Predicate;

interface WaitUntilMatched<T, R> {

    R waitUntil(Matcher<T>... matchers);

    R waitUntil(Predicate<T>... matchers);

}
