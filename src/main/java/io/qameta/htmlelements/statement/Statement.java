package io.qameta.htmlelements.statement;

/**
 * eroshenkoam
 * 10.03.17
 */
@FunctionalInterface
public interface Statement {

    Object evaluate() throws Throwable;

}
