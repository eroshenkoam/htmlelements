package io.qameta.htmlelements.locator;

import org.openqa.selenium.WebElement;

@FunctionalInterface
public interface WebElementValidator {

    boolean isValid(WebElement element);
}
