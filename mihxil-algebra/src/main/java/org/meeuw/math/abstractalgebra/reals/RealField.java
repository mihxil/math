package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.*;

/**
 * The field of real numbers.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealField extends AbstractAlgebraicStructure<RealNumber> implements NumberField<RealNumber> {

    public static final RealField INSTANCE = new RealField();

    private RealField() {
        super(RealNumber.class);
    }

    @Override
    public RealNumber zero() {
        return RealNumber.ZERO;
    }

    @Override
    public RealNumber one() {
        return RealNumber.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
