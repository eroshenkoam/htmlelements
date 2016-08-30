package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

public interface MethodHandler<R> {

    R handle(Context context);

}
