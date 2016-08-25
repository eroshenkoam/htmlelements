package io.qameta.htmlelements;

import io.qameta.htmlelements.example.page.AutoMainPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;

public class WebDriverTest {

    private WebDriver driver;

    @Rule
    public ExternalResource driverManager = new ExternalResource() {

        protected void before() throws Throwable {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
        }

        protected void after() {
            driver.close();
            driver.quit();
        }

    };

    @Test
    public void testOutput() {
        WebPageFactory factory = new WebPageFactory();
        AutoMainPage autoMainPage = factory.get(driver, AutoMainPage.class);
        autoMainPage.open("https://auto.ru/");
        autoMainPage.logo().hover()
                .should(displayed());
    }
}
