package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.Ring;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Integers implements Ring<IntegerElement> {

    public static final Integers INSTANCE = new Integers();

    private Integers() {
    }

    @Override
    public IntegerElement zero() {
        return new IntegerElement(0);
    }

    @Override
    public IntegerElement one() {
        return new IntegerElement(1);

    }
}
