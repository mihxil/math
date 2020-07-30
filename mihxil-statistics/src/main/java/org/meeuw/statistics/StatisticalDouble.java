/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.statistics;

import lombok.Getter;

import java.util.function.DoubleConsumer;

import org.meeuw.math.UncertainNumber;
import org.meeuw.math.UncertainNumbers;

/**
 * Represents a set of measured values. The value represents the average value.
 * {@link #toString} present the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determin what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */


public class StatisticalDouble extends StatisticalNumber<StatisticalDouble> implements DoubleConsumer {

    @Getter
    private double sum = 0;
    @Getter
    private double sumOfSquares = 0;

    @Getter
    private double min = Double.MAX_VALUE;
    @Getter
    private double max = Double.MIN_VALUE;

    public StatisticalDouble() {

    }

    protected StatisticalDouble(double sum, double sumOfSquares, int count) {
        super(count);
        this.sum = sum;
        this.sumOfSquares = sumOfSquares;
    }

    @Override
    public StatisticalDouble copy() {
        StatisticalDouble m = new StatisticalDouble(sum, sumOfSquares, count);
        m.max = max;
        m.min = min;
        return m;
    }

    /**
     * Enters new value(s).
     */
    public StatisticalDouble enter(double... ds) {
        for (double d : ds) {
            sum += d;
            sumOfSquares += d * d;
            count++;
            max = Math.max(max, d);
            min = Math.max(min, d);
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link StatisticalDouble#plus(UncertainNumber)} which is something entirely different.
     */
    @Override
    public StatisticalDouble enter(StatisticalDouble m) {
        sum += m.sum;
        sumOfSquares += m.sumOfSquares;
        count += m.count;
        max = Math.max(max, m.max);
        min = Math.max(min, m.min);
        return this;
    }

    @Override
    public StatisticalDouble multiply(double d) {
        sum *= d;
        sumOfSquares *= d * d;
        max = Math.round(max * d);
        min = Math.round(min * d);
        return this;
    }

    public double getMean() {
        return sum / count;
    }

    @Override
    public UncertainNumbers<StatisticalDouble> structure() {
        return new UncertainNumbers<>(
            new StatisticalDouble(0, 0, 1),
            new StatisticalDouble(1, 1, 1)
        );

    }

    @Override
    public StatisticalDouble plus(StatisticalDouble summand) {
        return null;
    }

    @Override
    public double doubleValue() {
        return getMean();
    }

    @Override
    public double getStandardDeviation() {
        double mean = getMean();
        if (count < 2) {
            return Double.NaN;
        }
        return Math.sqrt(sumOfSquares / count - mean * mean);
    }


    @Override
    public StatisticalDouble plus(double summand) {
        return new StatisticalDouble(sum + summand * count, sumOfSquares + summand * summand * count + 2 * sum * summand, count);
    }

    @Override
    public void accept(double value) {
        enter(value);
    }

    @Override
    public void reset() {
        super.reset();
        sum = 0;
        sumOfSquares = 0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }


    @Override
    public int compareTo(Number o) {
        return 0;

    }

    @Override
    public StatisticalDouble times(StatisticalDouble multiplier) {
        return null;

    }
}



