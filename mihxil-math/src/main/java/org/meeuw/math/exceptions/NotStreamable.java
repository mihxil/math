package org.meeuw.math.exceptions;

/**
 * E.g. some structures are only streamable when the underlying structures are.
 * @since 0.7
 */
public class NotStreamable extends IllegalStateException {

    public NotStreamable() {

    }

    public NotStreamable(String message) {
        super(message);
    }
}
