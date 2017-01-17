package io.qameta.htmlelements;

import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebPageTest {

    private WebPageFactory factory;

    private WebDriver driver;

    @Before
    public void initFactory() {
        this.driver = mock(WebDriver.class);
        this.factory = new WebPageFactory();
    }

    @Test
    public void webPageShouldHaveWrappedDriver() {
        WebPage page = factory.get(driver, WebPage.class);
        assertThat(page.getWrappedDriver(), equalTo(driver));
    }

    @Test
    public void webPageShouldProxySearchContextMethodsToDriver () {
        By searchCriteria = By.xpath("some-value");

        WebPage page = factory.get(driver, WebPage.class);
        page.findElement(searchCriteria);

        verify(driver).findElement(searchCriteria);
    }

    @Test()
    public void webPageShouldGoToBaseUrl() {
        String pageUrl = "http://www.base.url";

        try{
            SearchPage page = factory.get(driver, SearchPage.class);
            page.go();
        } catch (TimeoutException e) {
            assertThat(e.getMessage(),
                    containsString(format("Couldn't wait for page with url %s to load", pageUrl)));
        }
    }
}
