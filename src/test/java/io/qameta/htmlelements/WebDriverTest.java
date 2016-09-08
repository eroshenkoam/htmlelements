package io.qameta.htmlelements;

import io.qameta.htmlelements.example.page.SearchPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.core.IsNot.not;
import static ru.yandex.qatools.htmlelements.matchers.WebElementMatchers.exists;

public class WebDriverTest {

    @Test
    public void testOutput() {
        WebDriver driver = new ChromeDriver();
        SearchPage page = new WebPageFactory().get(driver, SearchPage.class);
        page.open("http://www.yandex.ru");
        page.searchArrow()
                .should(not(exists()));
    }
}
