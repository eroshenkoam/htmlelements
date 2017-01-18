package io.qameta.htmlelements.example.scroll;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.qameta.htmlelements.WebPageFactory;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.qameta.htmlelements.matcher.DisplayedMatcher.displayed;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author ehborisov
 */
public class ScrollTest {

    private WebDriver driver;

    @Before
    public void prepareDriver() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void scrollingTest() {
        WikiMainPage page = new WebPageFactory().get(driver, WikiMainPage.class);
        page.go();
        page.wikiList().scrollToElement();
        assertThat(page.wikiList(), displayed());
    }
}
