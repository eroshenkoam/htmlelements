package io.qameta.htmlelements.example.page;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Name;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.element.SearchArrow;
import io.qameta.htmlelements.WebPage;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface SearchPage extends WebPage {

    @FindBy(TestData.SEARCH_ARROW_XPATH)
    SearchArrow searchArrow();

}
