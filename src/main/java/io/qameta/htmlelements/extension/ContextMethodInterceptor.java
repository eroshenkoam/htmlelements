package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

public interface ContextMethodInterceptor<R> extends Extension {

    R intercept(Context context);

}
