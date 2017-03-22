package io.qameta.htmlelements.exception;

import org.openqa.selenium.WebDriverException;

/**
 * eroshenkoam
 * 22.03.17
 */
public class WaitUntilException extends WebDriverException {

    public WaitUntilException(String message) {
        super(message);
    }

}
