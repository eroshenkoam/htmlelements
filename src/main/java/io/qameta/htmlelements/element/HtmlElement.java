package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.Description;
import io.qameta.htmlelements.extension.Selector;

public interface HtmlElement extends ExtendedWebElement<HtmlElement> {

    @Selector
    String getSelector();

    @Description
    String getDescription();

}
