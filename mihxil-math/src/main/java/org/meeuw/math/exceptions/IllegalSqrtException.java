package org.meeuw.math.exceptions;

/**
 * Gets thrown if trying to take an impossible sqrt.
 */
public class IllegalSqrtException extends OperationException {

    public IllegalSqrtException(Throwable s) {
        super(s.getMessage());
        initCause(s);
    }
    public IllegalSqrtException(String mes) {
        super(mes);
    }
}
