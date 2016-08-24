package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.context.Context;

import java.lang.reflect.Method;

public interface ContextEnricher extends Extension {

    void enrich(Context context, Method method, Object[] args);

}
