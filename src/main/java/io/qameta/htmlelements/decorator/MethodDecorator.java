package io.qameta.htmlelements.decorator;

import org.openqa.selenium.SearchContext;

import java.lang.reflect.Method;

public interface MethodDecorator {

    boolean canDecorate(Method method);

    Object decorate(SearchContext searchContext, Method method);

}
