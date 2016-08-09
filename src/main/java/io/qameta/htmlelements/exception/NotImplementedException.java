package io.qameta.htmlelements.exception;

import java.lang.reflect.Method;

public class NotImplementedException extends RuntimeException {

    public NotImplementedException(Method method) {
        super(String.format("Method '%s' is not implemented", method.getName()));
    }

    static final long serialVersionUID = 1L;
}
