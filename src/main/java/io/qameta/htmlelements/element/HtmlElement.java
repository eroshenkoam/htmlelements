package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

public interface HtmlElement extends WebBlock, WebElement, Locatable,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

}
