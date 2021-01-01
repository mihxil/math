package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.DoubleConfidenceInterval;

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
    MetricSpaceElement<RealNumber, RealNumber>
{
    public static final int EPSILON_FACTOR = 2;

    public static final RealNumber ONE = new RealNumber(1d, 0);
    public static final RealNumber ZERO = new RealNumber(0d, 0);
    public static final RealNumber SMALLEST = new RealNumber(0d, Utils.uncertaintyForDouble(0));

    @Getter
    final double value;

    @Getter
    final double uncertainty;

    public static RealNumber of(double value) {
        return new RealNumber(value, uncertaintyForDouble(value));
    }

    public static RealNumber[] of(double... values) {
        return Arrays.stream(values)
            .mapToObj(RealNumber::of).toArray(RealNumber[]::new);
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
        return new RealNumber(
            value + summand.value,
            uncertainty + summand.uncertainty + uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber negation() {
        return new RealNumber(-1 * value, uncertainty);
    }

    @Override
    public RealNumber minus(RealNumber subtrahend) {
        return plus(negation());
    }

    @Override
    public RealNumber times(RealNumber multiplier) {
        if (multiplier == ONE) {
            return this;
        }
        return RealField.INSTANCE.considerMultiplicationByZero(this, multiplier);
    }

    protected boolean isExactlyZero() {
        return value == 0 && isExact();
    }
    protected boolean isExact() {
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
        return null;
    }

    @Override
    public boolean isOne() {
        return false;
    }

    @Override
    public int signum() {
        return (int) Math.signum(value);
    }


    public RealNumber times(double multiplier) {
        return new RealNumber(value * multiplier, uncertainty * Math.abs(multiplier));
    }

    @Override
    public RealNumber sin() {
        return new RealNumber(Math.sin(value), Math.max(uncertainty, Utils.uncertaintyForDouble(1)));
    }

    @Override
    public RealNumber cos() {
        return new RealNumber(Math.cos(value), Math.max(uncertainty, Utils.uncertaintyForDouble(1)));
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
        return null;
    }

    @Override
    public RealNumber sqrt() {
        return new RealNumber(Math.sqrt(value), uncertainty);
    }

    @Override
    public RealNumber pow(RealNumber exponent) {
        return new RealNumber(Math.pow(value, exponent.value), uncertainty);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealNumber that = (RealNumber) o;
        boolean result = getConfidenceInterval().contains(that.value);
        return result;
    }

    @Override
    public int hashCode() {
        return 0;
    }


    public DoubleConfidenceInterval getConfidenceInterval() {
        return DoubleConfidenceInterval.of(doubleValue(), getUncertainty(), EPSILON_FACTOR);
    }

    @Override
    public RealNumber abs() {
        return new RealNumber(Math.abs(value), uncertainty);
    }
}
