package io.qameta.htmlelements.extension.elements;

import io.qameta.htmlelements.context.Context;
import io.qameta.htmlelements.exception.WebPageException;
import io.qameta.htmlelements.extension.HandleWith;
import io.qameta.htmlelements.extension.MethodHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;

import static io.qameta.htmlelements.context.Store.DRIVER_KEY;

/**
 * @author ehborisov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HandleWith(SetFileInputMethod.Extension.class)
public @interface SetFileInputMethod {

    class Extension implements MethodHandler {

        @Override
        @SuppressWarnings("unchecked")
        public Object handle(Context context, Object proxy, Method method, Object[] args) throws Throwable {
            String fileName = (String) args[0];
            WebDriver driver = context.getStore().get(DRIVER_KEY, WebDriver.class)
                    .orElseThrow(() -> new WebPageException("WebDriver is missing"));

            if(driver.getClass().equals(RemoteWebDriver.class)){
                ((RemoteWebElement) proxy).setFileDetector(new LocalFileDetector());
            }

            String filePath = getFilePath(fileName);
            ((WebElement) proxy).sendKeys(filePath);
            return proxy;
        }

        private String getFilePath(final String fileName) {
            Optional<URL> resUrl = getResourceFromClasspath(fileName);
            return resUrl.map(URL::getPath).orElse(getPathForSystemFile(fileName));
        }

        private String getPathForSystemFile(final String fileName) {
            File file = new File(fileName);
            return file.getPath();
        }

        private static Optional<URL> getResourceFromClasspath(final String fileName) {
            return Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource(fileName));
        }
    }
}
