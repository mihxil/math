package org.meeuw.math.exceptions;

/**
 * Division by zero is mostly impossible. It's like multiplying by the reciprocal of {@link org.meeuw.math.abstractalgebra.AdditiveGroup#zero}
 * @author Michiel Meeuwissen
 */
public class DivisionByZeroException extends ReciprocalException {

    public DivisionByZeroException(String s) {
        super(s);
    }

    public DivisionByZeroException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }

    public DivisionByZeroException(Object e, Object divisor) {
        super("Division by zero exception: " + e + "/" + divisor);
    }

    public DivisionByZeroException(Object e, Object divisor, Throwable cause) {
        super("Division by zero exception: " + e + "/" + divisor);
        initCause(cause);
    }
}
