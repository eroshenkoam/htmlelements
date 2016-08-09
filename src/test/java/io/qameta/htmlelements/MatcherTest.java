package io.qameta.htmlelements;

import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matcher.HasTextMatcher.hasText;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        System.out.println(searchPage.searchArrow().suggest()
                .should(everyItem(displayed())));
    }
}