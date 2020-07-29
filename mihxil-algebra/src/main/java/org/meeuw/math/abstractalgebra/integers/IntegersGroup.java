package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.AdditiveGroup;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegersGroup implements AdditiveGroup<IntegerElement> {

    public static final IntegersGroup INSTANCE = new IntegersGroup();

    private IntegersGroup() {
    }

    @Override
    public IntegerElement zero() {
        return new IntegerElement(0);
    }
}
