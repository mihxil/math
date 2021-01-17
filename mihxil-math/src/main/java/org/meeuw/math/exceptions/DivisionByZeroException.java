package org.meeuw.math.exceptions;

/**
 * @author Michiel Meeuwissen
 */
public class DivisionByZeroException extends ReciprocalException {

    public DivisionByZeroException(String s) {
        super(s);
    }
    public DivisionByZeroException(Object e, Object divisor) {
        super("Division by zero exception: " + e + "/" + divisor);
    }

    public DivisionByZeroException(Object e, Object divisor, Throwable cause) {
        super("Division by zero exception: " + e + "/" + divisor);
        initCause(cause);
    }
}
