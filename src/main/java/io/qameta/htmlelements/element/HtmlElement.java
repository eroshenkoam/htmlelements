package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

public interface HtmlElement extends WebElement, HasName, WrapsElement,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

}
