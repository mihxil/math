package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.AbstractNumberElement;
import org.meeuw.math.abstractalgebra.NumberFieldElement;
import org.meeuw.math.uncertainnumbers.DoubleConfidenceInterval;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * A real number (backend by a double). No considerations for uncertainty propagation.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealNumber extends AbstractNumberElement<RealNumber> implements NumberFieldElement<RealNumber> {

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

    public RealNumber(double value, double uncertaintity) {
        this.value = value;
        if (Double.isNaN(uncertaintity)) {
            throw new IllegalArgumentException();
        }
        if (uncertaintity < 0) {
            throw new IllegalArgumentException();
        }
        this.uncertainty = uncertaintity;
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
    public RealNumber times(RealNumber multiplier) {
        if (multiplier == ONE) {
            return this;
        }
        double newValue = value * multiplier.value;
        if (newValue == 0) {
            return RealNumber.SMALLEST;
        }
        return new RealNumber(newValue,
            Math.abs(newValue) * (Math.abs(uncertainty / value) + Math.abs(multiplier.uncertainty / multiplier.value)) + uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber pow(int exponent) {
        double newValue = Math.pow(value, exponent);
        return new RealNumber(newValue,
            uncertainty * (Math.abs(exponent) *  Math.abs(Utils.pow(value, exponent - 1))) +  uncertaintyForDouble(newValue)
        );
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
        return new BigDecimal(value);
    }

    @Override
    public int signum() {
        return (int) Math.signum(value);
    }

    public RealNumber times(double multiplier) {
        return new RealNumber(value * multiplier, uncertainty * Math.abs(multiplier));
    }

    public RealNumber sin() {
        return new RealNumber(Math.sin(value), Math.max(uncertainty, Utils.uncertaintyForDouble(1)));
    }

    public RealNumber cos() {
        return new RealNumber(Math.cos(value), Math.max(uncertainty, Utils.uncertaintyForDouble(1)));
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(value, o.doubleValue());
    }

    @Override
    public int compareTo(RealNumber o) {
        if (getConfidenceInterval().contains(o.value) || o.getConfidenceInterval().contains(value)) {
            return 0;
        }
        return Double.compare(value, o.value);
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
        boolean result = getStructure().getEquivalence().test(this, that);
        return result;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public RealNumber epsilon() {
        return new RealNumber(EPSILON_FACTOR * uncertainty, 0);
    }

    public DoubleConfidenceInterval getConfidenceInterval() {
        return DoubleConfidenceInterval.of(doubleValue(), getUncertainty(), EPSILON_FACTOR);
    }
}
