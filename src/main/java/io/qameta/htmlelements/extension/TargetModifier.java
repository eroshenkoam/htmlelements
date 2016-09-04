package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

public interface TargetModifier<T> extends Extension {

    T modify (Context context, T target);

}
