package org.meeuw.math.exceptions;

/**
 * @author Michiel Meeuwissen
 */
public class DivisionByZeroException extends ReciprocalException {

    public DivisionByZeroException(ArithmeticException e) {
        this(e.getMessage());
        initCause(e);
    }
    public DivisionByZeroException(String s) {
        super(s);
    }
}
