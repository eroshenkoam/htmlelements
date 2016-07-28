package io.qameta.htmlelements.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class TestData {

    public static final String SEARCH_ARROW_XPATH = "//div[@class='search-arrow']";

    public static final String SUGGEST_XPATH = "//div[@class='suggest']";

    public static final String SEARCH_FORM_XPATH = "//div[@class='{{ className }}']";

    public static final String REQUEST_INPUT_XPATH = "//div[@class='input']";


    public static WebDriver mockDriver() {

        WebElement textInput = createWebElement("text input");
        when(textInput.isEnabled()).thenReturn(false, false, false, false, true);

        WebElement searchForm = createWebElement("search form");
        WebElement searchArrow = createWebElement("search arrow");

        List<WebElement> suggest = Arrays.asList(
                createWebElement("first suggest"),
                createWebElement("second suggest")
        );

        when(searchForm.findElement(By.xpath(REQUEST_INPUT_XPATH))).thenReturn(textInput);
        when(searchArrow.findElement(By.xpath(SEARCH_FORM_XPATH))).thenReturn(searchForm);
        when(searchArrow.findElements(By.xpath(SUGGEST_XPATH))).thenReturn(suggest);

        WebDriver driver = mock(WebDriver.class);
        when(driver.findElement(By.xpath(SEARCH_ARROW_XPATH))).thenReturn(searchArrow);

        return driver;
    }

    private static WebElement createWebElement (String text) {
        WebElement webElement = mock(WebElement.class);
        when(webElement.getText()).thenReturn(text);
        when(webElement.isDisplayed()).thenReturn(true);
        when(webElement.isEnabled()).thenReturn(true);
        return webElement;
    }
}
