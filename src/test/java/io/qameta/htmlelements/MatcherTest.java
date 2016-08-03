package io.qameta.htmlelements;

import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static io.qameta.htmlelements.matchers.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matchers.HasTextMatcher.hasText;

public class MatcherTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory(driver, ClassLoader.getSystemClassLoader());

        SearchPage searchPage = pageObjectFactory.get(SearchPage.class);
        searchPage.searchArrow()
                .waitUntil(displayed())
                .should(hasText("search-arrow"));

    }
}
