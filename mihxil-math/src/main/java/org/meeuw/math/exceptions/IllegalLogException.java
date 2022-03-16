package org.meeuw.math.exceptions;

/**
 * Gets thrown if trying to take an impossible logarithm.
 * @since 1.9
 */
public class IllegalLogException extends OperationException {

    public IllegalLogException(ArithmeticException s) {
        super(s);
    }

    public IllegalLogException(String s) {
        super(s);
    }
}
