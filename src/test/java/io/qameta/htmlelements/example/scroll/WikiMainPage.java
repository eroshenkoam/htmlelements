package io.qameta.htmlelements.example.scroll;

import io.qameta.htmlelements.WebPage;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.ScrollableElement;
import io.qameta.htmlelements.extension.page.BaseUrl;

/**
 * @author ehborisov
 */
@BaseUrl("https://en.wikipedia.org/wiki/Main_Page")
public interface WikiMainPage extends WebPage {

    @FindBy("//a[.='Complete list of Wikipedias']")
    ScrollableElement wikiList();
}
