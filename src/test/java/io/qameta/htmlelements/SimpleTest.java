package io.qameta.htmlelements;

import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class SimpleTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory(driver, ClassLoader.getSystemClassLoader());

        SearchPage searchPage = pageObjectFactory.get(SearchPage.class);

        System.out.println(searchPage.toString());
        System.out.println(searchPage.searchArrow().form("form").input().getText());

        System.out.println(searchPage.searchArrow().suggest().stream().findFirst().get().getText());
    }
}
