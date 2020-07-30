package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.Rng;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegers implements Rng<EvenIntegerElement> {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    private EvenIntegers() {
    }

    @Override
    public EvenIntegerElement zero() {
        return new EvenIntegerElement(0);
    }

}
