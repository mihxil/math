package org.meeuw.math.uncertainnumbers.field;

import lombok.Getter;

import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.uncertainnumbers.AbstractUncertainDouble;

/**
 * The most basic implementation of an {@link UncertainReal}. Immutable, based on {@code double}s.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleElement
    extends AbstractUncertainDouble<UncertainReal> implements UncertainReal {

    public static final UncertainDoubleElement ZERO = new UncertainDoubleElement(0, EXACT);
    public static final UncertainDoubleElement ONE  = new UncertainDoubleElement(1, EXACT);

    private final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    private final double value;
    @Getter
    private final double uncertainty;

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
        double newValue = getValue() * multiplier.getValue();
        return new UncertainDoubleElement(newValue,
            operations.multipliedUncertainty(newValue, getFractionalUncertainty(),  multiplier.getFractionalUncertainty()));
    }

    @Override
    public UncertainDoubleElement plus(UncertainReal summand) {
        return new UncertainDoubleElement(getValue() + summand.getValue(),
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
        return new UncertainDoubleElement(Math.sqrt(value), uncertainty);
    }

    public UncertainDoubleElement pow(UncertainDoubleElement uncertainDoubleElement) {
        return new UncertainDoubleElement(Math.pow(value, uncertainDoubleElement.getValue()), uncertainty);
    }

    @Override
    public UncertainDoubleElement sin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UncertainDoubleElement cos() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UncertainReal pow(UncertainReal exponent) {
        return new UncertainDoubleElement(
            Math.pow(getValue(), exponent.getValue()),
            operations.powerUncertainty(getValue(), getUncertainty(), exponent.getValue(), exponent.getUncertainty()));
    }

    @Override
    public  UncertainDoubleElement pow(int exponent) {
        return new UncertainDoubleElement(Math.pow(getValue(), exponent),
            Math.abs(exponent) * Math.pow(Math.abs(getValue()), exponent -1) * getUncertainty());
    }

    @Override
    public UncertainDoubleElement of(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
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

    @Override
    public UncertainDoubleElement abs() {
        return new UncertainDoubleElement(Math.abs(getValue()), uncertainty);
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return AlgebraicElementFormatProvider.toString(this);
    }


}
