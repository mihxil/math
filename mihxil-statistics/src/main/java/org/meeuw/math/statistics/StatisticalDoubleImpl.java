/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.statistics;

import lombok.Getter;

import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * Represents a set of measured values. The value represents the average value.
 * {@link #toString} present the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determine what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */

public class StatisticalDoubleImpl
    extends AbstractStatisticalDouble<StatisticalDoubleImpl>
    implements RealNumber, StatisticalDouble<StatisticalDoubleImpl>, DoubleConsumer {

    @Getter
    private double sum = 0;
    @Getter
    private double sumOfSquares = 0;

    @Getter
    private double min = Double.MAX_VALUE;
    @Getter
    private double max = -1 * Double.MAX_VALUE;

    public StatisticalDoubleImpl() {

    }

    protected StatisticalDoubleImpl(double sum, double sumOfSquares, int count) {
        super(count);
        this.sum = sum;
        this.sumOfSquares = sumOfSquares;
    }

    @Override
    public boolean isExact() {
        return min == max &&  doubleUncertainty() == EXACT;
    }

    @Override
    public StatisticalDoubleImpl copy() {
        StatisticalDoubleImpl m = new StatisticalDoubleImpl(sum, sumOfSquares, count);
        m.max = max;
        m.min = min;
        return m;
    }

    /**
     * Enters new value(s).
     * @param ds doubles to add
     * @return this
     */
    public StatisticalDoubleImpl enter(double... ds) {
        for (double d : ds) {
            sum += d;
            sumOfSquares += d * d;
            count++;
            max = Math.max(max, d);
            min = Math.min(min, d);
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link StatisticalDoubleImpl#plus(RealNumber)} which is something entirely different.
     */
    @Override
    public StatisticalDoubleImpl enter(StatisticalDoubleImpl m) {
        sum += m.sum;
        sumOfSquares += m.sumOfSquares;
        count += m.count;
        max = Math.max(max, m.max);
        min = Math.max(min, m.min);
        return this;
    }


    @Override
    public StatisticalDoubleImpl multiply(double d) {
        sum *= d;
        sumOfSquares *= d * d;
        max = DoubleUtils.round(max * d);
        min = DoubleUtils.round(min * d);
        return this;
    }

    @Override
    public OptionalDouble optionalDoubleMean() {
        if (count == 0) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of(sum / count);
        }
    }

    @Override
    public @NonNull RealField getStructure() {
        return RealField.INSTANCE;
    }


    @Override
    public RealNumber exp() {
        double value = Math.exp(getValue());
        return new DoubleElement(value, getUncertainty()); /// todo);
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value="Can't be taken of negative values")
    public RealNumber ln()  throws IllegalLogarithmException {
        UncertainNumber<Double> value = operations().ln(getValue());
        return new DoubleElement(value.getValue(), value.getUncertainty());
    }


    @Override
    public DoubleElement reciprocal() {
        if (getValue() == 0d) {
            throw new DivisionByZeroException("Division by zero", "1/" + getValue());
        }
        double value = 1d / getValue();
        return new DoubleElement(value, Math.abs(value) * getFractionalUncertainty() + DoubleUtils.uncertaintyForDouble(value));
    }


    @Override
    public double doubleStandardDeviation() {
        double mean = getMean();
        if (count < 2) {
            return Double.NaN;
        }
        return Math.sqrt(sumOfSquares / count - mean * mean);
    }

    @Override
    public StatisticalDoubleImpl plus(double summand) {
        return
            new StatisticalDoubleImpl(
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
    public StatisticalDoubleImpl abs() {
        if (isPositive()) {
            return this;
        } else {
            return new StatisticalDoubleImpl(-1 * sum, sumOfSquares, count);
        }
    }



}



