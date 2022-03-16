package org.meeuw.math.exceptions;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * Represents a problem with taking the inverse of an element. The must well known extension would be {@link DivisionByZeroException}.
 * But it is also thrown by {@link MultiplicativeGroupElement#reciprocal()} if this is exceptionaly not possible.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class InverseException extends OperationException {
    public InverseException(String s) {
        super(s);
    }

    public InverseException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }
}
