package io.qameta.htmlelements.example.page;

import io.qameta.htmlelements.WebPage;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.element.SearchArrow;
import io.qameta.htmlelements.extension.page.BaseUrl;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
@BaseUrl("http://www.base.url")
public interface SearchPage extends WebPage {

    @FindBy(TestData.SEARCH_ARROW_XPATH)
    SearchArrow searchArrow();

}
