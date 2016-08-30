package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Name;
import io.qameta.htmlelements.element.*;
import io.qameta.htmlelements.example.TestData;
import org.openqa.selenium.WebElement;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface SearchForm extends ExtendedWebElement<SearchForm> {

    @FindBy(TestData.REQUEST_INPUT_XPATH)
    WebElement input();

}
