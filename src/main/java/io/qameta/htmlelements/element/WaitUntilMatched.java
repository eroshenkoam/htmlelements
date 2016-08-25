package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

import java.util.function.Predicate;

public interface WaitUntilMatched<ReturnType> {

    ReturnType waitUntil(Predicate<ReturnType> predicate);

    default ReturnType waitUntil(String description, Predicate<ReturnType> predicate) {
        return waitUntil(predicate);
    }

    default ReturnType waitUntil(Matcher matcher) {
        return waitUntil(matcher.toString(), matcher::matches);
    }

}
