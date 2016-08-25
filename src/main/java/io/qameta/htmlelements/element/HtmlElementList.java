package io.qameta.htmlelements.element;

import java.util.List;
import java.util.function.Function;

public interface HtmlElementList<T> extends List<T>, WebBlock,
        ShouldMatched<HtmlElementList<T>>,
        WaitUntilMatched<HtmlElementList<T>>,
        FilterMatched<HtmlElementList<T>, T> {

    <R> HtmlElementList<R> convert(Function<T, R> function);
}
