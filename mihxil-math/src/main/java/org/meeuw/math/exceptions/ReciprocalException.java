package org.meeuw.math.exceptions;

/**
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
