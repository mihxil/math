package org.meeuw.math.exceptions;

public class IllegalSqrtException extends MathException {
    public IllegalSqrtException(Throwable s) {
        super(s.getMessage());
        initCause(s);
    }
    public IllegalSqrtException(String mes) {
        super(mes);
    }
}
