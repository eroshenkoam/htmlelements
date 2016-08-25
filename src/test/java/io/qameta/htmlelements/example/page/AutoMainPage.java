package io.qameta.htmlelements.example.page;

import io.qameta.htmlelements.WebPage;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.HtmlElement;

public interface AutoMainPage extends WebPage {

    @FindBy("//div[contains(@class, 'header__logo')]")
    HtmlElement logo();

}
