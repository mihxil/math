package org.meeuw.math.exceptions;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * Represents a problem with taking the reciprocal of an element. The must well known extension would be {@link DivisionByZeroException}.
 * But it is also thrown by {@link MultiplicativeGroupElement#reciprocal()} if this is exceptionaly not possible.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ReciprocalException extends MathException {
    public ReciprocalException(String s) {
        super(s);
    }

    public ReciprocalException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }
}
