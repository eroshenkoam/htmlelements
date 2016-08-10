package io.qameta.htmlelements;

import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;
import static io.qameta.htmlelements.matcher.HasTextMatcher.hasText;
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

        System.out.println(searchPage.searchArrow().suggest().filter(displayed()).size());
        System.out.println(searchPage.searchArrow().suggest().filter(displayed()).size());
        System.out.println(searchPage.searchArrow().suggest().filter(displayed()).size());
        System.out.println(searchPage.searchArrow().suggest().filter(displayed()).size());
    }


}