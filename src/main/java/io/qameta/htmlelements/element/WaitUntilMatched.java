package io.qameta.htmlelements.element;

import org.hamcrest.Matcher;

interface WaitUntilMatched<T, R>{

    R waitUntil(Matcher<T> matcher);

}
