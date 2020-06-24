/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

import java.util.function.DoubleConsumer;

/**
 * Represents a set of measurement values. The value represents the average value.
 * {@link #toString} present the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determin what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */


public class Measurement extends MeasurementNumber implements DoubleConsumer {

    private double sum = 0;
    private double squareSum = 0;


    public Measurement() {
    }

    protected Measurement(double sum, double squareSum, int count) {
        super(count);
        this.sum = sum;
        this.squareSum = squareSum;
    }

    /**
     * Enters new value(s).
     */
    public Measurement enter(double... ds) {
        for (double d : ds) {
            sum += d;
            squareSum += d * d;
            count++;
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link #add(Measurement)} which is something entirely different.
     */
    public Measurement enter(Measurement m) {
        sum += m.sum;
        squareSum += m.squareSum;
        count += m.count;
        return this;
    }

    public double getMean() {
        return sum / count;
    }


    @Override
    public double doubleValue() {
        return getMean();
    }


    @Override
    public double getStandardDeviation() {
        double mean = getMean();
        return Math.sqrt(squareSum / count - mean * mean);
    }

    public double getSum() {
        return sum;
    }

    public double getSumOfSquares() {
        return squareSum;
    }

    /**
     * Operator overloading would be very handy here, but java sucks.
     */
    public Measurement div(double d) {
        return new Measurement(sum / d, squareSum / (d * d), count);
    }

    public Measurement times(double d) {
        return new Measurement(sum * d, squareSum * (d * d), count);
    }

    public Measurement add(double d) {
        return new Measurement(sum + d * count, squareSum + d * d * count + 2 * sum * d, count);
    }

    /**
     * Assuming that this measurement is from a different set (the mean is <em>principally
     * different</em>)
     *
     * @todo Not yet correctly implemented
     */
    public Measurement add(Measurement m) {
        // think about this...
        return new Measurement(m.count * sum + count + m.sum, /* er */ 0, count * m.count);
    }

    @Override
    public void accept(double value) {
        enter(value);
    }


}



