package io.qameta.htmlelements;

import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;

import static io.qameta.htmlelements.matchers.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matchers.HasTextMatcher.hasText;
import static org.hamcrest.Matchers.hasSize;

public class MatcherTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory(driver, ClassLoader.getSystemClassLoader());

        SearchPage searchPage = pageObjectFactory.get(SearchPage.class);
        searchPage.searchArrow()
                .waitUntil(displayed())
                .should(hasText("search-arrow"));

        searchPage.searchArrow().suggest();

    }
}