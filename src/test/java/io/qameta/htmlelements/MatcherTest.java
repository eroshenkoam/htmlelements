package io.qameta.htmlelements;

import io.qameta.htmlelements.element.HtmlElement;
import io.qameta.htmlelements.element.HtmlElementList;
import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matcher.HasTextMatcher.hasText;
import static org.hamcrest.Matchers.*;

public class MatcherTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    @SuppressWarnings("unchecked")
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory();

        SearchPage searchPage = pageObjectFactory.get(driver, SearchPage.class);
//        searchPage.searchArrow()
//                .waitUntil("displayed", WebElement::isDisplayed)
//                .should(hasText("search-arrow"));

        System.out.println(searchPage.searchArrow().suggest().filter(WebElement::isDisplayed).size());
        System.out.println(searchPage.searchArrow().suggest().filter(WebElement::isDisplayed).size());
        System.out.println(searchPage.searchArrow().suggest().filter(WebElement::isDisplayed).size());
        System.out.println(searchPage.searchArrow().suggest().filter(WebElement::isDisplayed).size());

        searchPage.searchArrow().suggest()
                .filter(WebElement::isDisplayed)
                .should(hasSize(2));

        searchPage.searchArrow().suggest()
                .should(everyItem(notNullValue()));
    }


}