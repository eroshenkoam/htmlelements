package io.qameta.htmlelements.locator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Method;

public interface ElementLocatorFactory {

    ElementLocator createLocator(SearchContext searchContext, Annotations annotations);
}
