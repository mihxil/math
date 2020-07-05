/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

import lombok.Getter;

import java.util.function.DoubleConsumer;

/**
 * Represents a set of measurement values. The value represents the average value.
 * {@link #toString} present the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determin what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */


public class StatisticalMeasurement extends StatisticalMeasurementNumber<StatisticalMeasurement> implements DoubleConsumer {

    private double sum = 0;
    private double squareSum = 0;

    @Getter
    private double min = Double.MAX_VALUE;
    @Getter
    private double max = Double.MIN_VALUE;


    public StatisticalMeasurement() {
    }



    protected StatisticalMeasurement(double sum, double squareSum, int count) {
        super(count);
        this.sum = sum;
        this.squareSum = squareSum;
    }

    @Override
    StatisticalMeasurement copy() {
        StatisticalMeasurement m =  new StatisticalMeasurement(sum, squareSum, count);
        m.max = max;
        m.min = min;
        return m;
    }
    /**
     * Enters new value(s).
     */
    public StatisticalMeasurement enter(double... ds) {
        for (double d : ds) {
            sum += d;
            squareSum += d * d;
            count++;
            max = Math.max(max, d);
            min = Math.max(min, d);
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link #add(Measurement)} which is something entirely different.
     */
    @Override
    public StatisticalMeasurement enter(StatisticalMeasurement m) {
        sum += m.sum;
        squareSum += m.squareSum;
        count += m.count;
        max = Math.max(max, m.max);
        min = Math.max(min, m.min);
        return this;
    }


    @Override
    public StatisticalMeasurement multiply(double d) {
        sum *= d;
        squareSum *= d * d;
        max = Math.round(max * d);
        min = Math.round(min * d);
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
    @Override
    public StatisticalMeasurement div(double d) {
        return new StatisticalMeasurement(sum / d, squareSum / (d * d), count);
    }

    @Override
    public StatisticalMeasurement times(double d) {
        return new StatisticalMeasurement(sum * d, squareSum * (d * d), count);
    }

    public StatisticalMeasurement add(double d) {
        return new StatisticalMeasurement(sum + d * count, squareSum + d * d * count + 2 * sum * d, count);
    }



    @Override
    public void accept(double value) {
        enter(value);
    }

    @Override
    public void reset() {
        super.reset();
        sum = 0;
        squareSum = 0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }


}



