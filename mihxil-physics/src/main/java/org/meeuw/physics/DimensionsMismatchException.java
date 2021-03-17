package org.meeuw.physics;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class DimensionsMismatchException extends IllegalArgumentException {
    public DimensionsMismatchException(String s) {
        super(s);
    }
}
