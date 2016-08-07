package io.qameta.htmlelements;

import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.function.Predicate;

import static io.qameta.htmlelements.matchers.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matchers.HasTextMatcher.hasText;
import static org.hamcrest.Matchers.hasSize;

public class MatcherTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    @SuppressWarnings("unchecked")
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory();

        SearchPage searchPage = pageObjectFactory.get(driver, SearchPage.class);
        searchPage.searchArrow()
                .waitUntil(displayed())
                .should(displayed(), hasText("search-arrow"));

    }
}