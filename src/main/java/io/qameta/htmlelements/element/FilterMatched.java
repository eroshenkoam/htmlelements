package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

import java.util.function.Predicate;

public interface FilterMatched<ReturnType, PredicateType> {

    ReturnType filter(Predicate<PredicateType> predicate);

    default ReturnType filter(String description, Predicate<PredicateType> predicate) {
        return filter(predicate);
    }

    default ReturnType filter(Matcher matcher) {
        return filter(matcher::matches);
    }

}
