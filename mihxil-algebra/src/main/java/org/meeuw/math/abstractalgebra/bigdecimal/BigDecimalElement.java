package org.meeuw.math.abstractalgebra.bigdecimal;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement implements FieldElement<BigDecimalElement, BigDecimalField> {

    public static final BigDecimalElement ONE = new BigDecimalElement(BigDecimal.ONE);
    public static final BigDecimalElement ZERO = new BigDecimalElement(BigDecimal.ZERO);

    private final BigDecimal value;

    public BigDecimalElement(BigDecimal value) {
        this.value = value;
    }

    @Override
    public BigDecimalElement plus(BigDecimalElement summand) {
        return new BigDecimalElement(value.add(summand.value));
    }


    @Override
    public BigDecimalElement negation() {
        return new BigDecimalElement(value.negate());
    }

    @Override
    public BigDecimalElement times(BigDecimalElement multiplier) {
        return new BigDecimalElement(value.multiply(multiplier.value));
    }

    @Override
    public BigDecimalElement pow(int exponent) {
        return new BigDecimalElement(value.pow(exponent));
    }

    @Override
    public BigDecimalField structure() {
        return BigDecimalField.INSTANCE;

    }

    @Override
    public BigDecimalElement self() {
        return this;

    }
}
