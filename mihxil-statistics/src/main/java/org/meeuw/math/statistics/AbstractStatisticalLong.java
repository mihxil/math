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
import lombok.extern.java.Log;

import java.math.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.exceptions.OverflowException;
import org.meeuw.math.statistics.time.StatisticalDuration;
import org.meeuw.math.statistics.time.StatisticalInstant;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static java.lang.Math.*;

/**
 * Keeps tracks the sum and sum of squares of a sequence of long values.
 * <p>
 * It can work in different {@link Mode}s, which indicates how the long value itself must be interpreted.
 * Therefore, this implements {@link TemporalAmount} and {@link Temporal}, but related methods only work in the corresponding mode.
 * This is considered deprecated, though. Use {@link StatisticalInstant}, {@link StatisticalDuration} if the value must be interpreted as such.
 * <p>
 *
 *
 * @author Michiel Meeuwissen
 */
@SuppressWarnings("unchecked")
@Log
public abstract class AbstractStatisticalLong<SELF extends AbstractStatisticalLong<SELF>>
    extends
    AbstractStatisticalDouble<SELF>
    implements LongConsumer, IntConsumer {

    static final long SQUARE_SUM_FAILED = -1;

    protected long sum = 0;
    protected long squareSum = 0;

    @Getter
    protected long min = Long.MAX_VALUE;
    @Getter
    protected long max = Long.MIN_VALUE;

    // keep sum around zero, and squareSum not too big.
    boolean autoGuess = true;

    /**
     * The guessed mean is used to offset all values when adding them to the {@link #getUncorrectedSum()} and {@link #getUncorrectedSumOfSquares()}. It defaults to the first value {@link #enter(long...)}ed.
     * A better value can be determined at any time using {@link #reguess()}
     */
    @Getter
    protected long guessedMean = 0;

    protected double doubleOffset = 0d;

    public AbstractStatisticalLong() {

    }


    protected AbstractStatisticalLong(  long sum, long squareSum, int count, long guessedMean) {
        super(count);

        this.squareSum = squareSum;
        this.sum = sum;
        this.guessedMean = guessedMean;
    }

    @Override
    public SELF plus(double summand) {
        long rounded = DoubleUtils.round(summand);
        SELF result = plus(rounded);
        result.doubleOffset = (summand - (double) rounded);
        return result;
    }

    protected abstract SELF _copy();

    @Override
    public SELF copy() {
        SELF c = _copy();
        c.max = max;
        c.min = min;
        c.autoGuess = autoGuess;
        c.doubleOffset = doubleOffset;
        return c;
    }

    /**
     * Enters new value(s).
     * @param ds new values
     * @return this
     * @throws ArithmeticException If the sum goes over {@link Long#MAX_VALUE}
     */
    public SELF enter(long... ds) {
        for(long d : ds) {
            if (autoGuess) {
                guessedMean = d;
                autoGuess = false;
            }
            min = Math.min(min, d);
            max = Math.max(max, d);
            d -= guessedMean;
            sum = addExact(sum, d);
            if (squareSum !=  SQUARE_SUM_FAILED) {
                try {
                    squareSum = addExact(squareSum, multiplyExact(d, d));
                } catch (ArithmeticException ae) {
                    log.warning(ae.getMessage() + ": no square of sum any more. Standard deviation will not be available");
                    squareSum = SQUARE_SUM_FAILED;
                }
            }
            count++;

        }
        return (SELF) this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link AbstractStatisticalLong#plus(RealNumber)} which is something entirely different.
     * @param m The other {@link AbstractStatisticalLong} which value must be entered into this one
     */
    @Override
    public SELF enter(SELF m) {
        if (m.count == 0) {
            return (SELF) this;
        }
        if (this.count == 0) {
            this.sum = m.sum;
            this.squareSum = m.squareSum;
            this.guessedMean = m.guessedMean;
            this.count = m.count;
            return (SELF) this;
        }
        long diff = guessedMean - m.guessedMean;

        sum += m.sum - m.count * diff;
        if (squareSum !=  SQUARE_SUM_FAILED) {
            squareSum += m.getSumOfSquares(diff);
        }
        count += m.count;
        max = Math.max(max, m.max);
        min = Math.min(min, m.min);
        return (SELF) this;
    }


    @Override
    public OptionalDouble optionalDoubleMean() {
        if (count == 0) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of((double) guessedMean + ((double) sum / count) + doubleOffset);
        }
    }

    /**
     * @return The mean value as a {@link BigDecimal}, or {@link Optional#empty()} if no values entered yet.
     */
    public Optional<BigDecimal> getOptionalBigMean() {
        if (count == 0) {
            return Optional.empty();
        } else {
            if (doubleOffset != 0) {
                throw new IllegalStateException();
            }
            return Optional.of(
                BigDecimal.valueOf(guessedMean)
                    .add(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), NANO_PRECISION))
            );
        }
    }

    public long getRoundedMean() {
        return round(getMean());
    }

    @Override
    public void accept(int value) {
        enter(value);
    }

    @Override
    public RealNumber abs() {
        if (getValue() >= 0) {
            return this;
        } else {
            return new DoubleElement(getValue(), getUncertainty()).abs();
        }
    }





    /**
     * Rounds the underlying long to a multiple of 10, considering the current {@link #getStandardDeviation()}
     */
    protected long round(double in) {
        long orderOfMagnitude = IntegerUtils.positivePow10(DoubleUtils.log10(getStandardDeviation()));
        return DoubleUtils.round(in) / orderOfMagnitude * orderOfMagnitude;
    }

    @Override
    public @NonNull RealField getStructure() {
        return RealField.INSTANCE;
    }

    @Override
    public RealNumber exp() {
        return immutableInstance(Math.exp(getValue()), getUncertainty()/* TODO */);
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value="Can't be taken of negative values")
    public RealNumber ln() throws IllegalLogarithmException {
        UncertainNumber<Double> ln = operations().ln(getValue());
        return immutableInstance(
            ln.getValue(),
            Math.max(
                ln.getUncertainty(),
                operations.lnUncertainty(ln.getValue(), getUncertainty())
            )
        );
    }


    @Override
    public DoubleElement reciprocal() {
        UncertainNumber<Double> reciprocal = operations().reciprocal(getValue());
        double v = 1d / getValue();
        return immutableInstance(
            reciprocal.getValue(),
            Math.max(reciprocal.getUncertainty(), getFractionalUncertainty() * v + DoubleUtils.uncertaintyForDouble(v)));
    }


    public static MathContext NANO_PRECISION = new MathContext(6, RoundingMode.HALF_UP);
    public static long NANOS_IN_MILLIS = 1_000_000;
    public static BigDecimal BIG_NANOS_IN_MILLIS = BigDecimal.valueOf(NANOS_IN_MILLIS);



    @Override
    public Double getValue() {
        return doubleValue();
    }



    /**
     * @throws OverflowException If the sum of square overflowed
     */
    @Override
    public double doubleStandardDeviation() {
        if (count == 0) {
            return Double.NaN;
        }
        double mean = ((double) sum) / count;
        if (squareSum == SQUARE_SUM_FAILED) {
            throw new OverflowException("square sum overflowed", mean + ".sd");
        }
        double sq = ((double) squareSum / count) - mean * mean;
        return Math.sqrt(sq);
    }

    public long getSum() {
        return sum + count * guessedMean;
    }

    public long getSumOfSquares() {
        return getSumOfSquares(-1 * guessedMean);
    }

    protected long getSumOfSquares(long offset) {
        return addExact(
            subtractExact(
                squareSum,
                multiplyExact(multiplyExact(2, offset), sum)),
            multiplyExact(
                count,
                multiplyExact(offset, offset)
            )
        );
    }

    /**
     * This implementation keeps track of a 'guessedMean' value. The internal value {@link #getUncorrectedSumOfSquares()} is kept small like this, to avoid long overflows.
     * <p>
     * Calculating the {@link #doubleStandardDeviation()} ()} ()} happens using these 'uncorrected' (but smaller) versions, because the value should be the same. The actual sum of squares of all values is given by {@link #getSumOfSquares()}, which is the calculated but may more easily overflow.
     * @return the uncorrected sum
     * @see #getGuessedMean()
     */
    public long getUncorrectedSum() {
        return sum;
    }

    public long getUncorrectedSumOfSquares() {
        return squareSum;
    }


    @Override
    public SELF multiply(double d) {
        sum *= d;

        if (squareSum != SQUARE_SUM_FAILED) {
            squareSum = Math.multiplyExact(squareSum, (long) (d * d));
        }
        guessedMean *= d;
        max = DoubleUtils.round(max * d);
        min = DoubleUtils.round(min * d);
        return (SELF) this;
    }

    @Override
    public SELF multiply(long d) {
        sum *= d;

        if (squareSum != SQUARE_SUM_FAILED) {
            squareSum *= d * d;
        }
        guessedMean *= d;
        max *= d;
        min *= d;
        return (SELF) this;
    }

    public SELF add(long d) {
        return _add(d);
    }

    protected SELF _add(long d) {
        reguess();
        long dcount = d * count;
        if (squareSum != SQUARE_SUM_FAILED) {
            squareSum = addExact(squareSum, multiplyExact(d, addExact(dcount, multiplyExact(2, sum))));
        }
        sum += dcount;
        autoGuess = false;
        max = max + d;
        min = min + d;
        reguess();
        return (SELF) this;
    }




    public SELF plus(long d) {
        return copy()._add(d);
    }

    @Override
    public void accept(long value) {
        enter(value);
    }


    @Override
    public void reset() {
        super.reset();
        sum = 0;
        squareSum = 0;
        autoGuess = true;
        max = Long.MIN_VALUE;
        min = Long.MAX_VALUE;
    }

    /**
     * Uses the current {@link #getMean()} value as a new offset for values when keeping track of the sum and sum of squares of the values.
     * @return this
     */
    public SELF reguess() {
        final long newGuess = longValue();
        final long diff =  newGuess - guessedMean;
        if (this.squareSum != SQUARE_SUM_FAILED) {
            this.squareSum = getSumOfSquares(diff);
        }
        this.sum = sum - count * diff;
        this.guessedMean = newGuess;
        return (SELF) this;
    }

}



