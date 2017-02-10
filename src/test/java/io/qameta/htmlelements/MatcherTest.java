package io.qameta.htmlelements;

import io.qameta.htmlelements.example.element.SuggestItem;
import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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

        System.out.println(searchPage.searchArrow().toString());

        searchPage.searchArrow().suggest()
                .filter(WebElement::isDisplayed)
                .should("has size", hasSize(1));

        searchPage.searchArrow()
                .should("displayed", displayed());

        searchPage.searchArrow()
                .waitUntil("wait until", hasText("search-arrow"));

        searchPage.searchArrow().suggest()
                .convert(SuggestItem::title)
                .forEach(System.out::println);

        searchPage.searchArrow().suggest()
                .should(everyItem(notNullValue()));

    }

}