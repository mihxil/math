package org.meeuw.math.abstractalgebra.reals;

import java.math.MathContext;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.MathContextConfiguration;

/**
 * The algebra for {@link java.math.BigDecimal} (wrapped in {@link BigDecimalElement}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(CompleteField.class)
public class BigDecimalField
    extends AbstractAlgebraicStructure<BigDecimalElement>
    implements
    CompleteScalarField<BigDecimalElement>,
    MetricSpace<BigDecimalElement, BigDecimalElement> {

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
        return Cardinality.C;
    }

    public MathContext getMathContext() {
        return MathContextConfiguration.get().getContext();
    }

    @Override
    public String toString() {
        return "ℝ";
    }
}
