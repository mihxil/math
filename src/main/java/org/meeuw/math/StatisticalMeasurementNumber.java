/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

/**
 .
 *
 * @author Michiel Meeuwissen
 */


public abstract class StatisticalMeasurementNumber<T extends StatisticalMeasurementNumber<T>> extends Measurement {

    protected int count = 0;

    public StatisticalMeasurementNumber() {
    }
    protected StatisticalMeasurementNumber(int count) {
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

    abstract T copy();

    abstract double getStandardDeviation();

    @Override
    public double getUncertainty() {
        return getStandardDeviation();
    }

    public int getCount() {
        return count;
    }

    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     */
    public void setMinimumExponent(int m) {
        minimumExponent = m;
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
    public Measurement combine(Measurement m) {
        if (m instanceof StatisticalMeasurementNumber) {
            T copy = copy();
            return copy.enter((T) m);
        } else {
            return super.combine(m);
        }
    }

    public void reset() {
        count = 0;
    }

}



