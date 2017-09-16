package io.qameta.htmlelements;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.HtmlElement;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.matcher.DisplayedMatcher;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * eroshenkoam
 * 05.07.17
 */
public class GlobalParametersTest {

    @Test
    public void globalParametersTest() {
        WebDriver driver = TestData.mockDriver();
        WebElement element = TestData.WebElementBuilder.mockWebElement().withDisplayed(true).build();
        when(driver.findElement(By.xpath("//div[@class='value']"))).thenReturn(element);
        when(driver.findElement(By.xpath("//div[@class='simpleElement']"))).thenReturn(element);
        when(element.findElement(By.xpath(".//div[@class='childElement']"))).thenReturn(element);

        WebPageFactory factory = new WebPageFactory()
                .parameter("key", "value")
                .parameter("simple", "simpleElement")
                .parameter("child", "childElement");

        SimplePage page = factory.get(driver, SimplePage.class);
        page.input().should(DisplayedMatcher.displayed());
        page.simpleElement().should(DisplayedMatcher.displayed());
        page.simpleElement().childElement().should(DisplayedMatcher.displayed());
    }

    public interface SimplePage extends WebPage {

        @FindBy("//div[@class='{{ key }}']")
        HtmlElement input();

        @FindBy("//div[@class='{{ simple }}']")
        SimpleElement simpleElement();
    }

    public interface SimpleElement extends HtmlElement {

        @FindBy(".//div[@class='{{ child }}']")
        HtmlElement childElement();
    }

}
