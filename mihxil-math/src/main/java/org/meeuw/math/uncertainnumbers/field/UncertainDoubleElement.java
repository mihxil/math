package org.meeuw.math.uncertainnumbers.field;

import lombok.Getter;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.AbstractUncertainDouble;

/**
 * The most basic implementation of an {@link UncertainReal}. Immutable, based on primitive {@code double}s.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleElement
    extends AbstractUncertainDouble<UncertainReal> implements UncertainReal {

    public static final UncertainDoubleElement ZERO = exact(0);
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

    public static UncertainDoubleElement exact(double value) {
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
        return of(getValue() + summand.getValue(),
            operations.add(uncertainty, summand.getUncertainty()));
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

    public UncertainDoubleElement pow(UncertainDoubleElement uncertainDoubleElement) {
        return of(Math.pow(value, uncertainDoubleElement.getValue()), uncertainty);
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
        return of(
            Math.pow(getValue(), exponent.getValue()),
            operations.powerUncertainty(getValue(), getUncertainty(), exponent.getValue(), exponent.getUncertainty()));
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

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
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
