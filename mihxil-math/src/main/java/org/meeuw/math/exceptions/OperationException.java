package org.meeuw.math.exceptions;

/**
 * Indicating that some operation failed. E.g. negatives of sqrt's, inverses of zero. Those kind of things.
 *
 * The operation failed because it received some exceptional argument for which it is not possible.
 * @since 0.9
 */
public class OperationException extends MathException {

    public OperationException(String s) {
        super(s);
    }
}
