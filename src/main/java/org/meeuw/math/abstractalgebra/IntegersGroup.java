package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegersGroup implements AdditiveGroup<IntegerElement, IntegersGroup> {

    public static final IntegersGroup INSTANCE = new IntegersGroup();
    private IntegersGroup() {

    }
    @Override
    public IntegerElement zero() {
        return new IntegerElement(0);

    }
}
