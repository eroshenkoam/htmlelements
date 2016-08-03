package io.qameta.htmlelements.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public interface ElementLocatorFactory {

    ElementLocator createLocator(SearchContext searchContext, Annotations annotations);
}
