package org.meeuw.math.uncertainnumbers;


import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleField
    extends AbstractAlgebraicStructure<UncertainDoubleElement>
    implements Field<UncertainDoubleElement> {

    public static final UncertainDoubleField INSTANCE = new UncertainDoubleField();

    private UncertainDoubleField() {
        super(UncertainDoubleElement.class);
    }

    @Override
    public ImmutableUncertainDouble zero() {
        return ImmutableUncertainDouble.ZERO;
    }

    @Override
    public ImmutableUncertainDouble one() {
        return ImmutableUncertainDouble.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
