package io.qameta.htmlelements.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

public class DefaultElementLocatorFactory implements ElementLocatorFactory {

    @Override
    public ElementLocator createLocator(SearchContext searchContext, Method method) {
        return new SmartElementLocator(searchContext, method);
    }

}
