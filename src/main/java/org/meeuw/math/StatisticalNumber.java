/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.
*/

package org.meeuw.math;

/**
 * A 'statistic' measurement, can receive a number of values, and can calculate the average and standard deviation of those values.
 *
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 */


public abstract class StatisticalNumber<T extends StatisticalNumber<T>> extends UncertainNumber {

    protected int count = 0;

    public StatisticalNumber(Units units) {
        super(units);
    }
    protected StatisticalNumber(Units units, int count) {
        super(units);
        this.count = count;
    }

    @Override
    public long longValue() {
        return Math.round(doubleValue());
    }
    @Override
    public int intValue() {
        return (int) longValue();
    }
    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public byte byteValue() {
        return (byte) longValue();
    }
    @Override
    public short shortValue() {
        return (short) longValue();
    }

    public abstract T copy();

    abstract double getStandardDeviation();

    @Override
    public double getUncertainty() {
        return getStandardDeviation();
    }

    public int getCount() {
        return count;
    }


    public abstract T enter(T m);


    /**
     * Operator overloading would be very handy here, but java sucks.
     */
    public T divide(double d) {
        return multiply(1 / d);
    }

    public abstract T multiply(double d);

    @Override
    public T times(double d) {
        return copy().multiply(d);
    }
    public T div(double d) {
        return copy().divide(d);
    }

    @Override
    public UncertainNumber combine(UncertainNumber m) {
        if (m instanceof StatisticalNumber) {
            return combine((T) m);
        } else {
            return super.combine(m);
        }
    }

    public T combine(T m) {
        T copy = copy();
        return copy.enter(m);
    }

    public void reset() {
        count = 0;
    }

}



