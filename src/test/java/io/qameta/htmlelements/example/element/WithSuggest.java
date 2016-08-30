package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.ExtendedList;
import io.qameta.htmlelements.element.ExtendedWebElement;
import io.qameta.htmlelements.element.HtmlElement;
import io.qameta.htmlelements.element.HtmlElementList;
import io.qameta.htmlelements.example.TestData;

import java.util.List;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface WithSuggest<T> extends ExtendedWebElement<T> {

    @FindBy(TestData.SUGGEST_XPATH)
    ExtendedList<SuggestItem> suggest();

}
