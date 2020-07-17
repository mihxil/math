/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.
*/

package org.meeuw.math.statistics;

import lombok.Getter;

import java.util.LongSummaryStatistics;

/**
 * A 'statistic' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation and standard deviation of those values.
 *
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 */


public abstract class StatisticalNumber<T extends StatisticalNumber<T>> extends AbstractUncertainNumber<T> {

    /**
     * The total number of values which were {@link StatisticalDouble#enter(double...)}ed.
     */
    @Getter
    protected int count = 0;

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
  /*  *//**
     * A specialized version of {@link #combined(UncertainNumber)}, accepting and returning a {@code T}
     *//*
    public UncertainNumber<?> combined(UncertainNumber<?> m) {
        return super.combined(m);
    }*/

    @Override
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
    public ImmutableUncertainNumber plus(UncertainNumber<?> summand) {
        return immutableCopy().plus(summand);
    }

    @Override
    public ImmutableUncertainNumber times(UncertainNumber<?> multiplier) {
        return immutableCopy().times(multiplier);
    }

    @Override
    public T pow(int exponent) {
        return null;
    }

    public void reset() {
        count = 0;
    }

    public ImmutableUncertainNumber immutableCopy() {
        return new ImmutableUncertainNumber(doubleValue(), getUncertainty());
    }

}



