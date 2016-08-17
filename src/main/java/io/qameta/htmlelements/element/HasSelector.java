package io.qameta.htmlelements.element;

import io.qameta.htmlelements.annotation.Context;
import io.qameta.htmlelements.context.SelectorContextHandler;

public interface HasSelector {

    @Context(SelectorContextHandler.class)
    String getSelector();

}
