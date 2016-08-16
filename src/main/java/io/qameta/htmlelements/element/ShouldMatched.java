package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

interface ShouldMatched<T> {

    T should(Matcher matcher);

}
