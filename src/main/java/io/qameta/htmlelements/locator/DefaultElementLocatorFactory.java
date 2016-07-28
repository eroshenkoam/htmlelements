package io.qameta.htmlelements.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

public class DefaultElementLocatorFactory implements ElementLocatorFactory {

    @Override
    public ElementLocator createLocator(SearchContext searchContext, Annotations annotations) {
        return new SmartElementLocator(searchContext, annotations);
    }

}
