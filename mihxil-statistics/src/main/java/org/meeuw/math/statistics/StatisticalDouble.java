package org.meeuw.math.statistics;

import lombok.Getter;

import java.util.function.DoubleConsumer;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.IllegalLogException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.UncertainNumber;
import org.meeuw.math.uncertainnumbers.field.*;

/**
 * Represents a set of measured values. The value represents the average value.
 * {@link #toString} present the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determin what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */


public class StatisticalDouble extends StatisticalNumber<StatisticalDouble>
    implements DoubleConsumer {

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
     * @param ds doubles to add
     * @return this
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
     * See also {@link StatisticalDouble#plus(UncertainDouble)} which is something entirely different.
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
    public UncertainRealField getStructure() {
        return UncertainRealField.INSTANCE;
    }

    @Override
    public UncertainReal exp() {
        double value = Math.exp(getValue());
        return new UncertainDoubleElement(value, getUncertainty()); /// todo);
    }

    @Override
    @NonAlgebraic
    public UncertainReal ln()  throws IllegalLogException {
        UncertainNumber<Double> value = operations().ln(getValue());
        return new UncertainDoubleElement(value.getValue(), value.getUncertainty());
    }


    @Override
    public UncertainDoubleElement reciprocal() {
        if (getValue() == 0d) {
            throw new DivisionByZeroException("Division by zero");
        }
        double value = 1d / getValue();
        return new UncertainDoubleElement(value, value * getFractionalUncertainty() + Utils.uncertaintyForDouble(value));
    }

    @Override
    public double getValue() {
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
        return
            new StatisticalDouble(
                sum + summand * count,
                sumOfSquares + summand * summand * count + 2 * sum * summand, count);
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
    public UncertainReal abs() {
        if (isPositive()) {
            return this;
        } else {
            return new StatisticalDouble(-1 * sum, sumOfSquares, count);
        }
    }


}



