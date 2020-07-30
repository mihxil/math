package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.NumberField;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealField implements NumberField<RealNumber> {

    public static final RealField INSTANCE = new RealField();

    @Override
    public RealNumber zero() {
        return RealNumber.ZERO;
    }

    @Override
    public RealNumber one() {
        return RealNumber.ONE;
    }
}
