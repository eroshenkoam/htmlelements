package io.qameta.htmlelements.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.function.Predicate;

/**
 * eroshenkoam
 * 22.03.17
 */
public class PredicateMatcher<T> extends TypeSafeMatcher<T> {

    private final Predicate<T> predicate;

    public PredicateMatcher(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean matchesSafely(T t) {
        return predicate.test(t);
    }

    @Override
    public void describeTo(Description description) {

    }

}
