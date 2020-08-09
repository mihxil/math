package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.*;

/**
 * The algebra for {@link java.math.BigDecimal} (wrapped in {@link BigDecimalElement}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalField extends AbstractAlgebraicStructure<BigDecimalElement> implements NumberField<BigDecimalElement> {

    public static final BigDecimalField INSTANCE = new BigDecimalField();

    protected BigDecimalField() {
        super(BigDecimalElement.class);
    }

    @Override
    public BigDecimalElement zero() {
        return BigDecimalElement.ZERO;
    }

    @Override
    public BigDecimalElement one() {
        return BigDecimalElement.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
