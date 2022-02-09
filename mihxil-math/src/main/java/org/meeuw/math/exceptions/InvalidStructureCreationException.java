package org.meeuw.math.exceptions;

/**
 * Thrown if a structure is created that is invalid. E.g. a modulo structure with a negative divisor.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class InvalidStructureCreationException extends MathException {
    public InvalidStructureCreationException(String s) {
        super(s);
    }
}
