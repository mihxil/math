package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.MathContext;

import org.meeuw.math.abstractalgebra.*;

/**
 * The algebra for {@link java.math.BigDecimal} (wrapped in {@link BigDecimalElement}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalField
    extends AbstractAlgebraicStructure<BigDecimalElement>
    implements
    CompleteField<BigDecimalElement>,
    MetricSpace<BigDecimalElement, BigDecimalElement> {

    public static final BigDecimalField INSTANCE = new BigDecimalField();

    @Getter
    private final MathContext mathContext = MathContext.DECIMAL128;


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
}
