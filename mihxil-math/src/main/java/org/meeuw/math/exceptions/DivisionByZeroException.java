package org.meeuw.math.exceptions;

/**
 * @author Michiel Meeuwissen
 */
public class DivisionByZeroException extends ReciprocalException {
    public DivisionByZeroException(String s) {
        super(s);
    }
}
