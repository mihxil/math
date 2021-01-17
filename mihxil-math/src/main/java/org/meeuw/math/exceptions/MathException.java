package org.meeuw.math.exceptions;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class MathException extends ArithmeticException {
    public MathException(String s) {
        super(s);
    }
}
