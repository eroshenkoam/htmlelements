package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.elements.ScrollMethod;

/**
 * @author ehborisov
 */
public interface ScrollableElement extends ExtendedWebElement {

    @ScrollMethod
    void scrollToElement();
}
