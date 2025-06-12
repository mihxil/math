package org.meeuw.math.exceptions;

/**
 * Used in matrix classes, when trying to invert a not invertible matrix.
 * @since 0.14
 */
public class NotInvertibleException extends InvalidElementCreationException {
    public NotInvertibleException(String s) {
        super(s);
    }
}
