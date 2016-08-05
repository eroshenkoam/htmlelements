package io.qameta.htmlelements.element;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface HtmlElementList<T extends WebElement> extends List<T>,
        ShouldMatched<HtmlElementList<T>>,
        WaitUntilMatched<HtmlElementList<T>>,
        FilterMatched<HtmlElementList<T>> {

}
