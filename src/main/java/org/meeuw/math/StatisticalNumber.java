/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.
*/

package org.meeuw.math;

import lombok.Getter;

/**
 * A 'statistic' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation and standard deviation of those values.
 *
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 */


public abstract class StatisticalNumber<T extends StatisticalNumber<T>> extends UncertainNumber {

    /**
     * The total number of values which were {@link StatisticalDouble#enter(double...)}ed.
     */
    @Getter
    protected int count = 0;

    public StatisticalNumber(Units units) {
        super(units);
    }
    protected StatisticalNumber(Units units, int count) {
        super(units);
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
     * A specialized version of {@link #combine(UncertainNumber)}, accepting and returning a {@code T}
     */
    public T combine(T m) {
        T copy = copy();
        return copy.enter(m);
    }

    @Override
    public UncertainNumber combine(UncertainNumber m) {
        if (m instanceof StatisticalNumber) {
            return combine((T) m);
        } else {
            return super.combine(m);
        }
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
    public T times(double d) {
        return copy().multiply(d);
    }
    public T div(double d) {
        return copy().divide(d);
    }




    public void reset() {
        count = 0;
    }

}



