package org.meeuw.math.uncertainnumbers.field;


import org.meeuw.math.abstractalgebra.*;

/**
 * The field of {@link UncertainReal}'s
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainRealField
    extends AbstractAlgebraicStructure<UncertainReal>
    implements CompleteField<UncertainReal> {

    public static final UncertainRealField INSTANCE = new UncertainRealField();

    private UncertainRealField() {
        super(UncertainReal.class);
    }

    @Override
    public UncertainReal zero() {
        return UncertainDoubleElement.ZERO;
    }

    @Override
    public UncertainReal one() {
        return UncertainDoubleElement.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
