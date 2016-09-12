package io.qameta.htmlelements;

import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HoverTest {

    @Test
    public void testOutput() {
        WebDriver driver = new ChromeDriver();
        WebPageFactory pageObjectFactory = new WebPageFactory();

        SearchPage searchPage = pageObjectFactory.get(driver, SearchPage.class);
        searchPage.open("https://yandex.ru");
        searchPage.searchArrow().hover();

    }
}
