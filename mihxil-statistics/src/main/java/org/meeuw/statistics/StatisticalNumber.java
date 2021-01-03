package org.meeuw.statistics;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.LongSummaryStatistics;

import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A 'statistic' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation and standard deviation of those values.
 *
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 */


public abstract class StatisticalNumber<T extends StatisticalNumber<T> & UncertainReal> implements UncertainReal {

    /**
     * The total number of values which were {@link StatisticalDouble#enter(double...)}ed.
     */
    @Getter
    protected int count = 0;

    UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    public StatisticalNumber() {
    }
    protected StatisticalNumber(int count) {
        this.count = count;
    }

    public abstract T copy();

    abstract double getStandardDeviation();

    @Override
    public double getUncertainty() {
        return getStandardDeviation();
    }

    @Override
    public boolean isExact() {
        return false;
    }

    /**
     * Enters all values of another instance of this {@link StatisticalNumber}, effectively combining the given one into this one.
     */
    public abstract T enter(T m);

    /**
     * Synonymous to {@link #enter(StatisticalNumber)} (except the return value). Does the same though as e.g. {@link LongSummaryStatistics#combine(LongSummaryStatistics)}.
     */
    public void combine(T m) {
        enter(m);
    }

    public T combined(T m) {
        T copy = copy();
        return copy.enter(m);
    }

    /**
     * Divides the current instant by a value, and returns {@code this}
     */
    public T divide(double d) {
        return multiply(1 / d);
    }

    /**
     * Multiplies the current instant by a value, and returns {@code this}
     *
     * @see #times(double)
     */
    public abstract T multiply(double d);


    @Override
    public T times(double multiplier) {
        return copy().multiply(multiplier);
    }
    @Override
    public T dividedBy(double divisor) {
        return copy().divide(divisor);
    }

    @Override
    public T negation() {
        return times(-1);
    }


    @Override
    public UncertainDoubleElement pow(int exponent) {
        return new UncertainDoubleElement(Math.pow(getValue(), exponent), getUncertainty());
    }

    public void reset() {
        count = 0;
    }

    public UncertainDoubleElement immutableCopy() {
        return of(getValue(), getUncertainty());
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
        return 0;
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return AlgebraicElementFormatProvider.toString(this);
    }



    @Override
    public UncertainReal sqrt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UncertainReal sin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UncertainReal cos() {
        throw new UnsupportedOperationException();

    }

    @Override
    public UncertainReal pow(UncertainReal exponent) {
        return of(operations.pow(getValue(), exponent.getValue()),operations.powerUncertainty(getValue(), getUncertainty(), exponent.getValue(), exponent.getUncertainty()));
    }

    @Override
    public UncertainReal times(UncertainReal multiplier) {
        return of(getValue() * multiplier.getValue(), operations.multiplyUncertainty(multiplier.doubleValue(), getUncertainty()));
    }

    @Override
    public UncertainReal plus(UncertainReal summand) {
        return of(getValue() + summand.getValue(), operations.addUncertainty(getUncertainty(), summand.getUncertainty()));
    }

    @Override
    public long longValue() {
        return (long) getValue();
    }

    @Override
    public double doubleValue() {
        return getValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

    @Override
    public int compareTo(UncertainReal o) {
        return 0;
    }
}



