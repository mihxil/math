package org.meeuw.math.exceptions;

/**
 * Throws if you try to create an invalid element of some algebra. E.g. a rational number with zero denominator.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class InvalidElementCreationException extends InvalidOperationException {
    public InvalidElementCreationException(String s) {
        super(s);
    }

    public InvalidElementCreationException(NotASquareException notASquareException) {
        super(notASquareException.getMessage());
        initCause(notASquareException);
    }
}
