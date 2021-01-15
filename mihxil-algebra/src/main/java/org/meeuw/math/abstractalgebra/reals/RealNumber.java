package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.BigDecimal;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.uncertainnumbers.DoubleConfidenceInterval;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * A real number (backend by a double).
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealNumber
    implements
    CompleteFieldElement<RealNumber>,
    ScalarFieldElement<RealNumber>,
    MetricSpaceElement<RealNumber, RealNumber>,
    UncertainDouble<RealNumber>
{
    public static final int EPSILON_FACTOR = 2;
    public static final double UNCERTAINTY_FOR_ONE = Utils.uncertaintyForDouble(1);
    public static final double UNCERTAINTY_FOR_ZERO = Utils.uncertaintyForDouble(0);

    public static final RealNumber ONE = new RealNumber(1d, 0);
    public static final RealNumber ZERO = new RealNumber(0d, 0);
    public static final RealNumber SMALLEST = new RealNumber(0d, UNCERTAINTY_FOR_ZERO);

    @Getter
    final double value;

    @Getter
    final double uncertainty;

    public static RealNumber of(double value) {
        return new RealNumber(value, uncertaintyForDouble(value));
    }

    public RealNumber(double value, double uncertainty) {
        this.value = value;
        if (uncertainty < 0) {
            throw new IllegalArgumentException("Uncertainty cannot be negative");
        }
        this.uncertainty = uncertainty;
    }

    @Override
    public RealNumber plus(RealNumber summand) {
        double newValue = value + summand.value;
        return of(
            newValue,
            uncertainty + summand.uncertainty + uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber negation() {
        return of(-1 * value, uncertainty);
    }

    @Override
    public RealNumber minus(RealNumber subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    public RealNumber times(RealNumber multiplier) {
        if (multiplier == ONE) {
            return this;
        }
        return RealField.INSTANCE.considerMultiplicationBySpecialValues(this, multiplier);
    }

    protected boolean isExactlyZero() {
        return value == 0 && isExact();
    }
    @Override
    public boolean isExact() {
        return uncertainty == 0;
    }

    @Override
    public RealNumber pow(int exponent) {
        double newValue = Math.pow(value, exponent);
        return new RealNumber(newValue,
            uncertainty * (Math.abs(exponent) *  Math.abs(Utils.pow(value, exponent - 1))) +  uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber dividedBy(RealNumber divisor) {
        return times(divisor.reciprocal());
    }

    @Override
    public RealField getStructure() {
        return RealField.INSTANCE;
    }

    @Override
    public RealNumber reciprocal() {
        if (isZero()) {
            throw new ArithmeticException();
        }
        return pow(-1);
    }

    @Override
    public long longValue() {
        return Math.round(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value);
    }

    @Override
    public boolean isOne() {
        return isExact() && value == 1;
    }

    @Override
    public int signum() {
        return (int) Math.signum(value);
    }

    @Override
    public RealNumber of(double value, double uncertainty) {
        return new RealNumber(value, uncertainty);
    }

    @Override
    public RealNumber times(double multiplier) {
        return of(value * multiplier, uncertainty * Math.abs(multiplier));
    }

    @Override
    public RealNumber sin() {
        return of(operations().sin(value), Math.max(uncertainty, UNCERTAINTY_FOR_ONE));
    }

    @Override
    public RealNumber cos() {
        return of(operations().cos(value), Math.max(uncertainty, UNCERTAINTY_FOR_ONE));
    }

    @Override
    public RealNumber distanceTo(RealNumber otherElement) {
        return minus(otherElement).abs();
    }

    @Override
    public int compareTo(RealNumber o) {
        if (getConfidenceInterval().contains(o.value) || o.getConfidenceInterval().contains(value)) {
            return 0;
        }
        return Double.compare(value, o.value);
    }

    @Override
    public RealNumber sqr() {
        double sq = value * value;
        return of(sq, sq * getFractionalUncertainty() * 2);
    }

    @Override
    public RealNumber sqrt() {
        return of(Math.sqrt(value), Math.max(uncertainty, Utils.uncertaintyForDouble(value)));
    }

    @Override
    public RealNumber pow(RealNumber exponent) {
        return of(Math.pow(value, exponent.value), uncertainty);
    }

    @Override
    public RealNumber abs() {
        return of(Math.abs(value), uncertainty);
    }

    public DoubleConfidenceInterval getConfidenceInterval() {
        return DoubleConfidenceInterval.of(doubleValue(), getUncertainty(), EPSILON_FACTOR);
    }

    @Override
    public String toString() {
        return AlgebraicElementFormatProvider.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealNumber that = (RealNumber) o;
        return getConfidenceInterval().contains(that.value) || that.getConfidenceInterval().contains(value);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
