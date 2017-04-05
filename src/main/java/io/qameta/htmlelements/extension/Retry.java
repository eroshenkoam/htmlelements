package io.qameta.htmlelements.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * eroshenkoam
 * 22.03.17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    int timeoutInSeconds() default 5;

    int poolingInMillis() default 250;

    Class<? extends Throwable>[] ignoring();

}
