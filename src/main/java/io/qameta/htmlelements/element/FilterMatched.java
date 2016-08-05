package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

public interface FilterMatched<T> {

    T filter(Matcher... matcher);

}
