package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

public interface HtmlElement extends WebBlock, WebElement, Locatable,
        ShouldMatched<HtmlElement>,
        WaitUntilMatched<HtmlElement> {

    default HtmlElement hover() {
        Actions actions = new Actions(getContext().getDriver());
        actions.moveToElement(this).perform();
        return this;
    }

}
