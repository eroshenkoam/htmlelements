package io.qameta.htmlelements;

import org.openqa.selenium.WebDriver;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.*;

public class MatcherTest {

    private WebDriver driver = TestData.mockDriver();

    @Test
    @SuppressWarnings("unchecked")
    public void testOutput() throws Exception {
        WebPageFactory pageObjectFactory = new WebPageFactory();

        SearchPage searchPage = pageObjectFactory.get(driver, SearchPage.class);

        System.out.println(searchPage.searchArrow().getDriver());

        searchPage.searchArrow().suggest()
                .filter(WebElement::isDisplayed)
                .should(hasSize(2));

        searchPage.searchArrow().suggest()
                .should(everyItem(notNullValue()));
    }


}