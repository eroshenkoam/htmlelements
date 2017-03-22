package io.qameta.htmlelements.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * eroshenkoam
 * 22.03.17
 */
public class MethodParameters {

    private final Method method;

    private final Object[] args;

    public MethodParameters(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public <T> Optional<T> getParameter(Class<? extends Annotation> annotation, Class<T> type) {
        Parameter[] parameters = method.getParameters();
        T result = null;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(annotation)) {
                result = type.cast(args[i]);
            }
        }
        return Optional.ofNullable(result);
    }

}
