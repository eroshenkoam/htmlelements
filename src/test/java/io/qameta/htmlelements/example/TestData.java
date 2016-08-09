package io.qameta.htmlelements.example;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class TestData {

    public static final String SEARCH_ARROW_XPATH = "//div[@class='search-arrow']";

    public static final String SUGGEST_XPATH = "//div[@class='suggest']";

    public static final String SEARCH_FORM_XPATH = "//div[@class='form']";

    public static final String SEARCH_FORM_TEMPLATE = "//div[@class='{{ className }}']";

    public static final String REQUEST_INPUT_XPATH = "//div[@class='input']";


    public static WebDriver mockDriver() {

        WebElement textInput = createWebElement("text input", true, true);
        when(textInput.isEnabled()).thenReturn(false, false, false, false, true);

        WebElement searchForm = mock(WebElement.class);
        when(searchForm.getText()).then(new Answer<String>() {

            private int count = 0;

            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (count++ % 3 == 0) {
                    throw new Exception("");
                }
                return "search form";
            }
        });

        WebElement searchArrow = mock(WebElement.class);
        when(searchArrow.isDisplayed()).thenReturn(false, false, false, false, true);
        when(searchArrow.getText()).thenReturn("search-arro", "search", "search-arrow");

        List<WebElement> suggest = Arrays.asList(
                createWebElement("first suggest", true, true),
                createWebElement("second suggest", false, true)
        );

        when(searchForm.findElement(By.xpath(REQUEST_INPUT_XPATH))).thenReturn(textInput);
        when(searchArrow.findElement(By.xpath(SEARCH_FORM_XPATH))).thenReturn(searchForm);
        when(searchArrow.findElements(By.xpath(SUGGEST_XPATH))).thenReturn(suggest);

        WebDriver driver = mock(WebDriver.class);
        when(driver.findElement(By.xpath(SEARCH_ARROW_XPATH))).thenReturn(searchArrow);

        return driver;
    }

    private static WebElement createWebElement(String text, boolean displayed, boolean enabled) {
        WebElement webElement = mock(WebElement.class);
        when(webElement.getText()).thenReturn(text);
        when(webElement.toString()).thenReturn(text);
        when(webElement.isDisplayed()).thenReturn(displayed);
        when(webElement.isEnabled()).thenReturn(enabled);
        return webElement;
    }
}
