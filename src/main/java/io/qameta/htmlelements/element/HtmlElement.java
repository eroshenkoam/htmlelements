package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.Description;
import io.qameta.htmlelements.extension.Selector;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

public interface HtmlElement extends WebBlock, WebElement, Locatable,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

    @Selector
    String getSelector();

    @Description
    String getDescription();

    default HtmlElement hover() {
        Actions actions = new Actions(getContext().getDriver());
        actions.moveToElement(this);
        return this;
    }

}
