package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;

public interface HtmlElement extends WebElement,
        ShouldMatched<WebElement, HtmlElement>,
        WaitUntilMatched<WebElement, HtmlElement> {

}
