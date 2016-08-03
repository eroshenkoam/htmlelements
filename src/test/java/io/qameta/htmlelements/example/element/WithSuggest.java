package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.*;
import io.qameta.htmlelements.example.TestData;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface WithSuggest extends HtmlElement {

    @FindBy(TestData.SUGGEST_XPATH)
    List<HtmlElement> suggest();

}
