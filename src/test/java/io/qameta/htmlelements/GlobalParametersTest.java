package io.qameta.htmlelements;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.element.HtmlElement;
import io.qameta.htmlelements.example.TestData;
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
        WebElement element = TestData.WebElementBuilder.mockWebElement().build();
        when(driver.findElement(By.xpath("//div[@class='value']"))).thenReturn(element);

        WebPageFactory factory = new WebPageFactory()
                .parameter("key", "value");

        SimplePage page = factory.get(driver, SimplePage.class);
        assertThat(page.input(), notNullValue());

    }

    public interface SimplePage extends WebPage {

        @FindBy("//div[@class='{{ key }}']")
        HtmlElement input();

    }



}
