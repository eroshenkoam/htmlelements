package io.qameta.htmlelements.context;

import java.util.function.Function;

public interface ContextHandler<T> extends Function<Context, T> {

    @Override
    T apply(Context context);
}
