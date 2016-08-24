package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.Description;
import io.qameta.htmlelements.extension.Selector;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

public interface HtmlElement extends WebBlock, WebElement, WrapsElement,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

    @Selector
    String getSelector();

    @Description
    String getDescription();

}
