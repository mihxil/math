package org.meeuw.math.abstractalgebra.reals;

import java.math.BigDecimal;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.AbstractNumberElement;
import org.meeuw.math.abstractalgebra.NumberFieldElement;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.NumberOperations;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 *  A real number (backend by a big decimal)
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement extends AbstractNumberElement<BigDecimalElement>
    implements NumberFieldElement<BigDecimalElement>, UncertainNumber<BigDecimal> {

    public static final BigDecimalElement ONE = new BigDecimalElement(BigDecimal.ONE, BigDecimal.ZERO);
    public static final BigDecimalElement ZERO = new BigDecimalElement(BigDecimal.ZERO,  BigDecimal.ZERO);
    public static final BigDecimalElement PI = new BigDecimalElement(new BigDecimal(Utils.PI), new BigDecimal("1e-100"));
    public static final BigDecimalElement e = new BigDecimalElement(new BigDecimal(Utils.e), new BigDecimal("1e-100"));

    private final BigDecimal value;
    private final BigDecimal uncertainty;

    public static BigDecimalElement of(double doubleValue){
        return new BigDecimalElement(BigDecimal.valueOf(doubleValue), uncertainty(doubleValue));
    }
    public static BigDecimalElement of(String stringValue){
        return new BigDecimalElement(new BigDecimal(stringValue), BigDecimal.ZERO);
    }

    public static BigDecimal uncertainty(double doubleValue) {
        BigDecimal u = BigDecimal.valueOf(doubleValue / 1e16);
        return BigDecimal.ONE.scaleByPowerOfTen(u.precision() - u.scale());
    }

    public BigDecimalElement(BigDecimal value, BigDecimal uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }
    public BigDecimalElement(UncertainNumber<BigDecimal> value) {
        this.value = value.getValue();
        this.uncertainty = value.getUncertainty();
    }


    @Override
    public BigDecimalElement plus(BigDecimalElement summand) {
        return new BigDecimalElement(UncertainNumber.super.plus(summand));
    }

    @Override
    public BigDecimalElement negation() {
        return new BigDecimalElement(value.negate(), uncertainty);
    }

    @Override
    public int compareTo(BigDecimalElement compare) {
        return value.compareTo(compare.value);
    }

    @Override
    public BigDecimalElement times(BigDecimalElement multiplier) {
        return new BigDecimalElement(UncertainNumber.super.times(multiplier));
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public BigDecimal getUncertainty() {
        return uncertainty;
    }

    @Override
    public NumberOperations<BigDecimal> operations() {
        return BigDecimalOperations.INSTANCE;
    }

    @Override
    public BigDecimalElement pow(int exponent) {
        if (exponent < 0) {
            return ONE.dividedBy(pow(-1 * exponent));
        } else {
            return new BigDecimalElement(value.pow(exponent), uncertainty);
        }
    }

    @Override
    public BigDecimalElement reciprocal() {
        //return new BigDecimalElement(UncertainNumber.super.reciprocal());

        return new BigDecimalElement(BigDecimal.ONE.divide(value, getStructure().getMathContext()), uncertainty);
    }

    @Override
    public BigDecimalField getStructure() {
        return BigDecimalField.INSTANCE;
    }

    public BigDecimalElement times(double multiplier) {
        return new BigDecimalElement(
            new BigDecimal(value.doubleValue() * multiplier),
            uncertainty.multiply(new BigDecimal(multiplier))
        );

    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return value;
    }

    @Override
    public int signum() {
        return value.signum();
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
        return equals(that, 1);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
