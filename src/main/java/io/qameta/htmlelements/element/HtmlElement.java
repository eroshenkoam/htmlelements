package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.DescriptionMethod;
import io.qameta.htmlelements.extension.SelectorMethod;

public interface HtmlElement extends ExtendedWebElement<HtmlElement> {

    @SelectorMethod
    String getSelector();

    @DescriptionMethod
    String getDescription();

}
