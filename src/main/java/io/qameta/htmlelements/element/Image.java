package io.qameta.htmlelements.element;

/**
 * @author ehborisov
 */
public interface Image extends ExtendedWebElement {

    default String getSource() {
       return getAttribute("src");
    }

    default String getAlt() {
        return getAttribute("alt");
    }
}
