package io.qameta.htmlelements.example;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static io.qameta.htmlelements.example.TestData.WebElementBuilder.mockWebElement;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class TestData {

    public static final String SEARCH_ARROW_XPATH = "//div[@class='search-arrow']";

    public static final String SUGGEST_XPATH = "//div[@class='suggest']";

    public static final String SUGGEST_ITEM_XPATH = "//a";

    public static final String SEARCH_FORM_XPATH = "//div[@class='form']";

    public static final String SEARCH_FORM_TEMPLATE = "//div[@class='{{ className }}']";

    public static final String REQUEST_INPUT_XPATH = "//div[@class='input']";


    public static WebDriver mockDriver() {

        WebElement textInput = mockWebElement()
                .withText("text input")
                .withDisplayed(true)
                .build();

        List<WebElement> suggest = Arrays.asList(
                mockWebElement()
                        .withChildElement(SUGGEST_ITEM_XPATH, mockWebElement().withText("first suggest item").build())
                        .withText("first suggest")
                        .withDisplayed(false, true)
                        .build(),
                mockWebElement()
                        .withChildElement(SUGGEST_ITEM_XPATH, mockWebElement().withText("second suggest item").build())
                        .withText("second suggest")
                        .withDisplayed(false, false, false, true)
                        .build()
        );

        WebElement searchForm = mockWebElement()
                .withChildElement(REQUEST_INPUT_XPATH, textInput)
                .withText("searchForm")
                .build();

        WebElement searchArrow = mockWebElement()
                .withChildElements(SUGGEST_XPATH, suggest)
                .withChildElement(SEARCH_FORM_XPATH, searchForm)
                .withText("search-arro", "search", "search-arrow")
                .withDisplayed(true)
                .build();


        WebDriver driver = mock(WebDriver.class);
        when(driver.findElement(By.xpath(SEARCH_ARROW_XPATH))).thenReturn(searchArrow);

        return driver;
    }

    private static WebElement createWebElement(String text, Boolean displayed, Boolean... displayedNext) {
        WebElement webElement = mock(WebElement.class);
        when(webElement.getText()).thenReturn(text);
        when(webElement.toString()).thenReturn(text);
        when(webElement.isDisplayed()).thenReturn(displayed, displayedNext);
        return webElement;
    }

    public static class WebElementBuilder {

        public static WebElementBuilder mockWebElement() {
            return new WebElementBuilder();
        }

        private final WebElement element;

        public WebElementBuilder() {
            this.element = mock(WebElement.class);
        }

        public WebElementBuilder withText(String text, String... other) {
            when(getElement().getText()).thenReturn(text, other);
            return this;
        }

        public WebElementBuilder withDisplayed(boolean displayed, Boolean... other) {
            when(getElement().isDisplayed()).thenReturn(displayed, other);
            return this;
        }

        public WebElementBuilder withChildElement(String xpath, WebElement element) {
            when(getElement().findElement(By.xpath(xpath))).thenReturn(element);
            return this;
        }

        public WebElementBuilder withChildElements(String xpath, List<WebElement> elements) {
            when(getElement().findElements(By.xpath(xpath))).thenReturn(elements);
            return this;
        }

        private WebElement getElement() {
            return element;
        }

        public WebElement build() {
            return getElement();
        }

    }
}
