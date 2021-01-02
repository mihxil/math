package org.meeuw.math.abstractalgebra.reals;

import java.math.*;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 *  A real number (backend by a big decimal)
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement
    implements ScalarFieldElement<BigDecimalElement>, CompleteFieldElement<BigDecimalElement>, MetricSpaceElement<BigDecimalElement, BigDecimalElement>,
    UncertainNumber<BigDecimal> {

    public static final BigDecimalElement ONE = new BigDecimalElement(BigDecimal.ONE, BigDecimal.ZERO);

    public static final BigDecimalElement ZERO = new BigDecimalElement(BigDecimal.ZERO,  BigDecimal.ZERO);
    public static final BigDecimalElement PI = new BigDecimalElement(new BigDecimal(Utils.PI), new BigDecimal("1e-" + (Utils.PI.length() - 1)));
    public static final BigDecimalElement e = new BigDecimalElement(new BigDecimal(Utils.e), new BigDecimal("1e-" +  (Utils.e.length() - 1)));

    private final BigDecimal value;
    private final BigDecimal uncertainty;

    public static BigDecimalElement of(double doubleValue){
        return new BigDecimalElement(BigDecimal.valueOf(doubleValue), uncertainty(doubleValue));
    }
    public static BigDecimalElement of(String stringValue){
        return new BigDecimalElement(new BigDecimal(stringValue), BigDecimal.ZERO);
    }

    public static BigDecimal uncertainty(double doubleValue) {
        return new BigDecimal(2).pow(Utils.leastSignifantBit(doubleValue), new MathContext(2, RoundingMode.UP));
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
    public BigDecimalElement minus(BigDecimalElement n1) {
        return new BigDecimalElement(
            value.subtract(n1.value),
            operations().addUncertainty(uncertainty, n1.uncertainty)
        );
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
    public BigDecimalElement sqr() {
        return new BigDecimalElement(value.multiply(value), uncertainty.multiply(uncertainty));
    }

    @Override
    public BigDecimalElement dividedBy(BigDecimalElement n) {
        return new BigDecimalElement(operations().divide(value, n.value), operations().multiplyUncertainty(uncertainty, n.uncertainty));
    }

    @Override
    public BigDecimalElement sqrt() {
        return new BigDecimalElement(operations().sqrt(value), operations().sqrt(uncertainty));
    }

    @Override
    public BigDecimalElement pow(BigDecimalElement bigDecimalElement) {
        return new BigDecimalElement(operations().pow(value, bigDecimalElement.value), uncertainty);
    }

    @Override
    public BigDecimalElement sin() {
        return new BigDecimalElement(operations().sin(value), uncertainty);
    }

    @Override
    public BigDecimalElement cos() {
        return new BigDecimalElement(operations().cos(value), uncertainty);
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
    public UncertaintyNumberOperations<BigDecimal> operations() {
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
    public boolean isOne() {
        return value.equals(BigDecimal.ONE);
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    //@Override
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
        return 0;
    }

    @Override
    public BigDecimalElement abs() {
        return new BigDecimalElement(value.abs(), uncertainty);
    }

    @Override
    public BigDecimalElement distanceTo(BigDecimalElement otherElement) {
        return new BigDecimalElement(getValue().min(otherElement.getValue()).abs(), getUncertainty());
    }
}
