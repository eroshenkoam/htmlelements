package io.qameta.htmlelements.element;


import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

public interface HtmlElement extends WebElement,
        ShouldMatched<WebElement, HtmlElement>,
        WaitUntilMatched<WebElement, HtmlElement> {

    HtmlElement should(Matcher<WebElement> matcher);

    HtmlElement waitUntil(Matcher<WebElement> matcher);

}
