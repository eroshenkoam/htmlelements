package io.qameta.htmlelements.element;

/**
 * @author ehborisov
 */
public interface Link extends ExtendedWebElement {

    default String getReference() {
        return getAttribute("href");
    }
}
