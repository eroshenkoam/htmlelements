package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.SelectorProvider;

public interface HtmlElement extends ExtendedWebElement<HtmlElement> {

    @SelectorProvider
    String getSelector();

    @DescriptionProvider
    String getDescription();

}
