package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.ExtendedWebElement;
import io.qameta.htmlelements.element.HtmlElement;
import io.qameta.htmlelements.example.TestData;

public interface SuggestItem extends ExtendedWebElement<SuggestItem>{

    @FindBy(TestData.SUGGEST_ITEM_XPATH)
    HtmlElement title();

}
