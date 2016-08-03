package io.qameta.htmlelements.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class DefaultElementLocatorFactory implements ElementLocatorFactory {

    @Override
    public ElementLocator createLocator(SearchContext searchContext, Annotations annotations) {
        return new DefaultElementLocator(searchContext, annotations);
    }

}
