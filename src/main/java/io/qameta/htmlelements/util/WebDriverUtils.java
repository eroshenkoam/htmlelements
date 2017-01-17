package io.qameta.htmlelements.util;

import io.qameta.htmlelements.exception.WebPageException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author ehborisov
 */
public class WebDriverUtils {

    public static boolean pageIsLoaded(WebDriver webDriver) {
        if (webDriver instanceof JavascriptExecutor) {
                Object result = ((JavascriptExecutor) webDriver)
                        .executeScript("if (document.readyState) return document.readyState;");
                return result != null && "complete".equals(result);
        } else {
            throw new WebPageException("Driver must support javascript execution");
        }
    }
}
