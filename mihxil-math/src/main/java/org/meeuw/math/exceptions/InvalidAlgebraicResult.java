package org.meeuw.math.exceptions;

/**
 * Indicates that no valid algebraic result was returned from an some operations.
 *
 * E.g. the result of an operation should never by {@code null}. If it is, this exception may get thrown.
 *
 * @since 0.8
 */
public class InvalidAlgebraicResult extends MathException {
    public InvalidAlgebraicResult(String s) {
        super(s);
    }
}
