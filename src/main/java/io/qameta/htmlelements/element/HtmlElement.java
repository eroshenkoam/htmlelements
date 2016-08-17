package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

public interface HtmlElement extends HasName, HasSelector, WebElement, WrapsElement,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

}
