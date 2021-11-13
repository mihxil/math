package org.meeuw.math.uncertainnumbers.field;

import lombok.Getter;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.AbstractUncertainDouble;

/**
 * The most basic implementation of an {@link UncertainReal}. Immutable, based on primitive {@code double}s.
 *
 * The structure is {@link UncertainRealField}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleElement
    extends AbstractUncertainDouble<UncertainReal> implements UncertainReal {

    public static final UncertainDoubleElement ZERO = new UncertainDoubleElement(0, EXACT) {
        @Override
        public UncertainDoubleElement sqrt() {
            return this;
        }
        @Override
        public UncertainDoubleElement sqr() {
            return this;
        }
        @Override
        public UncertainDoubleElement pow(int exponent) {
            if (exponent == 0) {
                return ONE;
            }
            return this;
        }
    };
    public static final UncertainDoubleElement ONE  = new UncertainDoubleElement(1, EXACT) {
        @Override
        public UncertainDoubleElement sqrt() {
            return this;
        }
        @Override
        public UncertainDoubleElement sqr() {
            return this;
        }
        @Override
        public UncertainDoubleElement pow(int exponent) {
            return this;
        }
    };

    private static final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    private final double value;
    @Getter
    private final double uncertainty;

    public static UncertainDoubleElement exactly(double value) {
        return new UncertainDoubleElement(value, EXACT);
    }

    public static UncertainDoubleElement of(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
    }

    public UncertainDoubleElement(double value, double uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public UncertainRealField getStructure() {
        return UncertainRealField.INSTANCE;
    }

    @Override
    public boolean eq(UncertainReal other) {
        return equals(other);
    }

    @Override
    public UncertainDoubleElement times(UncertainReal multiplier) {
        if (multiplier.isOne()){
            return this;
        }
        double newValue = getValue() * multiplier.getValue();
        return of(newValue,
            operations.multipliedUncertainty(newValue, getFractionalUncertainty(),  multiplier.getFractionalUncertainty()));
    }

    @Override
    public UncertainDoubleElement plus(UncertainReal summand) {
        double v1 = getValue();
        double v2 = summand.getValue();
        double result  = v1 + v2;
        return of(
            result,
            Utils.max(
                operations.add(uncertainty, summand.getUncertainty()),
                Utils.uncertaintyForDouble(result),
                Utils.uncertaintyForDouble(v1),
                Utils.uncertaintyForDouble(v2)
            )
        );
    }

    @Override
    public UncertainDoubleElement reciprocal() {
        return pow(-1);
    }

    @Override
    public UncertainReal negation() {
        return times(-1);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public UncertainDoubleElement sqrt() {
        return of(Math.sqrt(value), uncertainty);
    }

    @Override
    public UncertainDoubleElement sin() {
        return of(operations().sin(value), uncertainty);
    }

    @Override
    public UncertainDoubleElement cos() {
        return of(operations().cos(value), uncertainty);
    }

    @Override
    public UncertainReal pow(UncertainReal exponent) {
        double result = Math.pow(value, exponent.getValue());
        return of(
            result,
            Utils.max(
                Utils.uncertaintyForDouble(result),
                operations.powerUncertainty(
                    value,
                    Math.max(uncertainty, Utils.uncertaintyForDouble(value)),
                    exponent.getValue(),
                    Math.max(exponent.getUncertainty(), Utils.uncertaintyForDouble(exponent.getValue())),
                    result
                )
            ));
    }

    @Override
    public  UncertainDoubleElement pow(int exponent) {
        double v = getValue();
        if (v == 0 && exponent < 0) {
            throw new DivisionByZeroException(v + "^" + exponent);
        }
        return of(
            Math.pow(getValue(), exponent),
            Math.abs(exponent) * Math.pow(Math.abs(getValue()), exponent -1) * getUncertainty());
    }

    @Override
    public UncertainDoubleElement abs() {
        return of(Math.abs(getValue()), uncertainty);
    }

    @Override
    public UncertainDoubleElement _of(double value, double uncertainty) {
        return of(value, uncertainty);
    }

    @SuppressWarnings({"EqualsDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object o) {
        return equals(o, 1);
    }

    @Override
    public int hashCode() {
        // must return constant to ensure that this is consistent with equals
        return 0;
    }

    public int compareTo(Number o) {
        return Double.compare(getValue(), o.doubleValue());
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return FormatService.toString(this);
    }


}
