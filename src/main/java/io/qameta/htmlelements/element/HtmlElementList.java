package io.qameta.htmlelements.element;

import java.util.List;

public interface HtmlElementList<T> extends List<T>, WebBlock,
        ShouldMatched<HtmlElementList<T>>,
        WaitUntilMatched<HtmlElementList<T>>,
        FilterMatched<HtmlElementList<T>> {

}
