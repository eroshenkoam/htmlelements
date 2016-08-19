package io.qameta.htmlelements.element;

public interface WebBlock extends HasContext {

    default String getName() {
        return getContext().getName();
    }

    default String getSelector() {
        return getContext().getSelector();
    }

}
