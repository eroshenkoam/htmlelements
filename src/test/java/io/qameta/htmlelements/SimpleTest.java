package io.qameta.htmlelements;

import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;
import static org.hamcrest.Matchers.everyItem;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class SimpleTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory();

        SearchPage searchPage = pageObjectFactory.get(driver, SearchPage.class);

        System.out.println(searchPage.searchArrow().form("form").getText());
        System.out.println(searchPage.searchArrow().form("form").input().getText());

        searchPage.searchArrow().suggest()
                .waitUntil(everyItem(displayed()));
    }
}
