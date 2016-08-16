package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

interface WaitUntilMatched<T> {

    T waitUntil(Matcher matcher);

}
