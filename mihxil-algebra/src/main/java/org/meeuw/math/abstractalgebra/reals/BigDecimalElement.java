package org.meeuw.math.abstractalgebra.reals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.meeuw.math.abstractalgebra.AbstractNumberElement;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 *  A real number (backend by a big decimal)
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement extends AbstractNumberElement<BigDecimalElement> implements NumberFieldElement<BigDecimalElement> {

    public static final BigDecimalElement ONE = new BigDecimalElement(BigDecimal.ONE);
    public static final BigDecimalElement ZERO = new BigDecimalElement(BigDecimal.ZERO);

    private final BigDecimal value;

    public static BigDecimalElement of(double doubleValue){
        return new BigDecimalElement(BigDecimal.valueOf(doubleValue));
    }

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
    public int compareTo(BigDecimalElement compare) {
        return value.compareTo(compare.value);
    }

    @Override
    public BigDecimalElement times(BigDecimalElement multiplier) {
        return new BigDecimalElement(value.multiply(multiplier.value));
    }

    @Override
    public BigDecimalElement pow(int exponent) {
        if (exponent < 0) {
            return ONE.dividedBy(pow(-1 * exponent));
        } else {
            return new BigDecimalElement(value.pow(exponent));
        }
    }

    @Override
    public BigDecimalElement reciprocal() {
        return new BigDecimalElement(BigDecimal.ONE.divide(value, MathContext.DECIMAL32));
    }

    @Override
    public BigDecimalField getStructure() {
        return BigDecimalField.INSTANCE;
    }

    @Override
    public BigDecimalElement self() {
        return this;
    }

    public BigDecimalElement times(double multiplier) {
        return new BigDecimalElement(new BigDecimal(value.doubleValue() * multiplier));
    }

    @Override
    public int intValue() {
        return value.intValueExact();
    }

    @Override
    public long longValue() {
        return value.longValueExact();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public int compareTo(Number o) {
        if (o instanceof BigDecimal) {
            return value.compareTo((BigDecimal) o);
        } else {
            return value.compareTo(BigDecimal.valueOf(o.doubleValue()));
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigDecimalElement that = (BigDecimalElement) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
