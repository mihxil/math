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
import org.meeuw.math.*;
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

    /**
     * When just adding an (exact) number, we can preserve information by just shifting the entire thing.
     */
    private double offset = 0;



    @Getter
    private double min = Double.MAX_VALUE;
    @Getter
    private double max = -1 * Double.MAX_VALUE;

    public StatisticalDoubleImpl() {

    }

    protected StatisticalDoubleImpl(double sum, double sumOfSquares, int count, double offset) {
        super(count);
        this.sum = sum;
        this.sumOfSquares = sumOfSquares;
        this.offset = offset;
    }

    @Override
    public boolean isExact() {
        return min == max &&  doubleUncertainty() == EXACT;
    }

    @Override
    public StatisticalDoubleImpl copy() {
        StatisticalDoubleImpl m = new StatisticalDoubleImpl(sum, sumOfSquares, count, offset);
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
            d -= offset;
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
        if (m.offset != offset) {
            m = m.copy().normalize();
            normalize();
        }
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
        max *= d;
        min *= d;
        offset *= d;
        return this;
    }


    @Override
    public StatisticalDoubleImpl multiply(long d) {
        sum *= d;
        sumOfSquares *= d * d;
        max *= d;
        min *= d;
        offset *= d;
        return this;
    }




    @Override
    public OptionalDouble optionalDoubleMean() {
        if (count == 0) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of(offset + sum / count);
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
        double mean = sum / count;
        if (count < 2) {
            return Double.NaN;
        }
        return Math.max(
            DoubleUtils.uncertaintyForDouble(mean),
            Math.sqrt(sumOfSquares / count - mean * mean)
        );
    }

    @Override
    public StatisticalDoubleImpl plus(double summand) {
        return new StatisticalDoubleImpl(
              sum,
                sumOfSquares,
                count,
                offset + summand);
    }

    @Override
    public StatisticalDoubleImpl dividedBy(double divisor) {
        return new StatisticalDoubleImpl(
            sum / divisor,
        sumOfSquares / (divisor * divisor),
        count,
        offset / divisor);
    }
     @Override
    public StatisticalDoubleImpl dividedBy(long divisor) {
        return new StatisticalDoubleImpl(
            sum / divisor,
        sumOfSquares / (divisor * divisor),
        count,
        offset / divisor);
    }

    @Override
    public StatisticalDoubleImpl times(double multiplier) {
        return new StatisticalDoubleImpl(
            sum * multiplier,
        sumOfSquares * multiplier * multiplier,
            count,
            offset * multiplier
        );
    }

    protected StatisticalDoubleImpl normalize() {
        double sum = this.sum;
        this.sum +=  this.offset * count;
        this.sumOfSquares += 2 * sum * this.offset + this.offset * this.offset * this.count;
        this.offset = 0;
        return this;
    }

    @Override
    public void accept(double value) {
        enter(value - offset);
    }

    @Override
    public void reset() {
        super.reset();
        sum = 0;
        sumOfSquares = 0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        offset = 0;
    }

     @Override
     public int signum() {
        return (int) Math.signum(offset + sum / count);
     }

    @Override
    public StatisticalDoubleImpl abs() {
        if (isPositive()) {
            return this;
        } else {
            return new StatisticalDoubleImpl(
                -1 * sum,
                sumOfSquares,
                count,
                -1 * offset
            );
        }
    }



}



