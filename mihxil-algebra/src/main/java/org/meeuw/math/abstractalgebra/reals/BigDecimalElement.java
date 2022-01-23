package org.meeuw.math.abstractalgebra.reals;

import java.math.*;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 *  A real number (backend by a big decimal)
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement implements
    ScalarFieldElement<BigDecimalElement>,
    CompleteFieldElement<BigDecimalElement>,
    MetricSpaceElement<BigDecimalElement, BigDecimalElement>,
    UncertainNumber<BigDecimal> {

    private static final MathContext UNCERTAINTY_MATH_CONTEXT= new MathContext(2, RoundingMode.HALF_UP);

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
        return of(new BigDecimal(stringValue));
    }

    public static BigDecimalElement of(BigDecimal bigDecimal){
        return new BigDecimalElement(bigDecimal, BigDecimal.ZERO);
    }

    public static BigDecimal uncertainty(double doubleValue) {
        return BigDecimal.valueOf(Utils.uncertaintyForDouble(doubleValue));
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
        UncertainNumber<BigDecimal> newValue = operations().divide(value, n.value);
        return new BigDecimalElement(newValue.getValue(),
            operations().multipliedUncertainty(
                newValue.getValue(),
                getFractionalUncertainty(),
                n.getFractionalUncertainty()
            ).max(newValue.getUncertainty())
        );

    }

    @Override
    public BigDecimalElement sqrt() {
        UncertainNumber<BigDecimal> sqrt = operations().sqrt(value);
        return new BigDecimalElement(sqrt.getValue(),
            operations().sqrt(uncertainty).getValue().max(sqrt.getUncertainty())
        );
    }

    @Override
    public BigDecimalElement pow(BigDecimalElement bigDecimalElement) throws ReciprocalException {
        UncertainNumber<BigDecimal> pow = operations().pow(value, bigDecimalElement.value);
        return new BigDecimalElement(pow.getValue(), uncertainty.max(sin().getUncertainty()));
    }

    @Override
    public BigDecimalElement sin() {
        UncertainNumber<BigDecimal> sin = operations().sin(value);
        return new BigDecimalElement(sin.getValue(), uncertainty.max(sin.getUncertainty()));
    }


    @Override
    public BigDecimalElement cos() {
        UncertainNumber<BigDecimal> cos = operations().cos(value);
        return new BigDecimalElement(cos.getValue(), uncertainty.max(cos.getUncertainty()));
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
        return uncertainty.round(UNCERTAINTY_MATH_CONTEXT);
    }


    @Override
    public BigDecimalOperations operations() {
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
        try {
            BigDecimal newValue = BigDecimal.ONE.divide(value, getStructure().getMathContext());
            return new BigDecimalElement(newValue, getFractionalUncertainty().multiply(newValue));
        } catch (ArithmeticException ae) {
            throw new DivisionByZeroException(BigDecimal.ONE, value, ae);
        }
    }

    @Override
    public BigDecimalField getStructure() {
        return BigDecimalField.INSTANCE;
    }

    @Override
    public BigDecimalElement dividedBy(long divisor) {
        UncertainNumber<BigDecimal> newValue = operations().divide(value, BigDecimal.valueOf(divisor));
        return new BigDecimalElement(
            newValue.getValue(),
            uncertainty.divide(BigDecimal.valueOf(divisor), operations().uncertaintyContext()).max(
                newValue.getUncertainty()
            )
        );
    }

    @Override
    public BigDecimalElement times(long multiplier) {
         return new BigDecimalElement(
            value.multiply(BigDecimal.valueOf(multiplier)),
            uncertainty.multiply(BigDecimal.valueOf(multiplier))
        );
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

    @Override
    public BigDecimalElement abs() {
        return new BigDecimalElement(value.abs(), uncertainty);
    }

    @Override
    public BigDecimalElement distanceTo(BigDecimalElement otherElement) {
        return new BigDecimalElement(
            getValue().subtract(otherElement.getValue()).abs(), getUncertainty());
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
    public String toString() {
        return value.toString();
    }


}
