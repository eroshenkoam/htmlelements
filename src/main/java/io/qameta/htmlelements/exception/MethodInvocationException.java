package io.qameta.htmlelements.exception;

public class MethodInvocationException extends RuntimeException{

    public MethodInvocationException (ReflectiveOperationException e) {
        super(e);
    }

    public Throwable getCause() {
        return super.getCause().getCause();
    }
}
