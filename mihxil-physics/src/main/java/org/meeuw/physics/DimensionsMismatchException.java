package org.meeuw.physics;

import org.meeuw.math.exceptions.NotComparableException;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class DimensionsMismatchException extends NotComparableException {
    public DimensionsMismatchException(String s) {
        super(s);
    }
}
