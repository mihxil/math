package org.meeuw.math.abstractalgebra.doubles;

import org.meeuw.math.abstractalgebra.NumberField;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DoubleField implements NumberField<DoubleElement, DoubleField> {

    public static final DoubleField INSTANCE = new DoubleField();

    @Override
    public DoubleElement zero() {
        return DoubleElement.ZERO;

    }

    @Override
    public DoubleElement one() {
        return DoubleElement.ONE;
    }
}
