package io.qameta.htmlelements.context;

public class SelectorContextHandler implements ContextHandler<String> {

    @Override
    public String apply(Context context) {
        return context.getSelector();
    }
}
