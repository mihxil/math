package org.meeuw.math.uncertainnumbers;


import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainNumbersField
    extends AbstractAlgebraicStructure<UncertainNumberElement>
    implements Field<UncertainNumberElement> {

    public static final UncertainNumbersField INSTANCE = new UncertainNumbersField();

    private UncertainNumbersField() {
        super(UncertainNumberElement.class);
    }

    @Override
    public ImmutableUncertainNumber zero() {
        return ImmutableUncertainNumber.ZERO;
    }

    @Override
    public ImmutableUncertainNumber one() {
        return ImmutableUncertainNumber.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
