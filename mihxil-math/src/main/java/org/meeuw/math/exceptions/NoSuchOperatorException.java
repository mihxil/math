package org.meeuw.math.exceptions;

public class NoSuchOperatorException extends InvalidOperationException {

    public NoSuchOperatorException(String s) {
        super(s);
    }

    public NoSuchOperatorException(String s, Throwable noSuchMethodException) {
        super(s);
        initCause(noSuchMethodException);
    }

    public NoSuchOperatorException(IllegalArgumentException iae) {
        super(iae.getMessage());
        initCause(iae);
    }
}
