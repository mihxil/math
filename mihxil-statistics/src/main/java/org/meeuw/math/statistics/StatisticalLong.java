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
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.*;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.exceptions.OverflowException;
import org.meeuw.math.temporal.StatisticalTemporal;
import org.meeuw.math.uncertainnumbers.UncertainNumber;
import org.meeuw.math.uncertainnumbers.field.*;

import static java.lang.Math.*;
import static org.meeuw.math.temporal.UncertainTemporal.Mode.LONG;

/**
 * Keeps tracks the sum and sum of squares of a sequence of long values.
 * <p>
 * It can work in different {@link Mode}s, which indicates how the long value itself must be interpreted.
 *
 * @author Michiel Meeuwissen
 */
@Log
public class StatisticalLong extends
    AbstractStatisticalDouble<StatisticalLong>
    implements LongConsumer, IntConsumer, StatisticalTemporal<StatisticalLong, Double, UncertainReal> {

    static final long SQUARE_SUM_FAILED = -1;

    private long sum = 0;
    private long squareSum = 0;
    @Getter
    @NonNull
    private final Mode mode;
    @Getter
    private long min = Long.MAX_VALUE;
    @Getter
    private long max = Long.MIN_VALUE;

    // keep sum around zero, and squareSum not too big.
    boolean autoGuess = true;

    /**
     * The guessed mean is used to offset all values when adding them to the {@link #getUncorrectedSum()} and {@link #getUncorrectedSumOfSquares()}. It defaults to the first value {@link #enter(long...)}ed.
     * A better value can be determined at any time using {@link #reguess()}
     */
    @Getter
    private long guessedMean = 0;

    private double doubleOffset = 0d;

    public StatisticalLong() {
        this(null);
    }

    public StatisticalLong(@Nullable Mode mode) {
        this.mode = mode == null ? LONG : mode;
    }

    protected StatisticalLong(@NonNull Mode mode, long sum, long squareSum, int count, long guessedMean) {
        super(count);
        this.mode = mode;
        this.squareSum = squareSum;
        this.sum = sum;
        this.guessedMean = guessedMean;
    }

    @Override
    public StatisticalLong plus(double summand) {
        long rounded = DoubleUtils.round(summand);
        StatisticalLong result = plus(rounded);
        result.doubleOffset = (summand - (double) rounded);
        return result;
    }

    @Override
    public StatisticalLong copy() {
        StatisticalLong c = new StatisticalLong(mode, sum, squareSum, count, guessedMean);
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
    public StatisticalLong enter(long... ds) {
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
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link StatisticalLong#plus(UncertainReal)} which is something entirely different.
     * @param m The other {@link StatisticalLong} which value must be entered into this one
     */
    @Override
    public StatisticalLong enter(StatisticalLong m) {
        if (m.count == 0) {
            return this;
        }
        if (this.count == 0) {
            this.sum = m.sum;
            this.squareSum = m.squareSum;
            this.guessedMean = m.guessedMean;
            this.count = m.count;
            return this;
        }
        long diff = guessedMean - m.guessedMean;

        sum += m.sum - m.count * diff;
        squareSum += m.getSumOfSquares(diff);
        count += m.count;
        max = Math.max(max, m.max);
        min = Math.min(min, m.min);
        return this;
    }


    @Override
    public OptionalDouble optionalDoubleMean() {
        if (count == 0) {
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of((double) guessedMean + ((double) sum / count) + doubleOffset);
        }
    }

    public Optional<BigDecimal> getOptionalBigMean() {
        if (count == 0){
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
    public UncertainReal abs() {
        if (getValue() >= 0) {
            return this;
        } else {
            return new UncertainDoubleElement(getValue(), getUncertainty()).abs();
        }
    }

    protected long round(double in) {
        long orderOfMagnitude = IntegerUtils.positivePow10(DoubleUtils.log10(getStandardDeviation()));
        return DoubleUtils.round(in) / orderOfMagnitude * orderOfMagnitude;
    }

    @Override
    public UncertainRealField getStructure() {
        return UncertainRealField.INSTANCE;
    }

    @Override
    public UncertainReal exp() {
        return immutableInstance(Math.exp(getValue()), getUncertainty()/* TODO */);
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value="Can't be taken of negative values")
    public UncertainReal ln() throws IllegalLogarithmException {
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
    public UncertainDoubleElement reciprocal() {
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
    public Optional<Instant> optionalInstantValue() {
        return getOptionalBigMean()
            .map(bd -> {
                final BigDecimal nanoTime = bd.multiply(BigDecimal.valueOf(NANOS_IN_MILLIS));
                final BigDecimal[] bigDecimals = nanoTime.divideAndRemainder(BIG_NANOS_IN_MILLIS);
                return Instant.ofEpochMilli(bigDecimals[0].longValue())
                    .plusNanos(
                        bigDecimals[1].longValue()
                    );
            });
    }


    @Override
    public Optional<Duration> optionalDurationValue() {
        return getOptionalBigMean()
            .map(bd -> {
                switch(mode) {
                    case DURATION:
                        return Duration
                            .ofMillis(bd.longValue())
                            .plusNanos(
                                bd.remainder(BigDecimal.ONE).multiply(BIG_NANOS_IN_MILLIS).longValue()
                            );
                    case DURATION_NS:
                        return Duration.ofNanos(bd.longValue());
                    default: throw new IllegalStateException();
                }
            });
    }


    /**
     * @throws OverflowException If sum of square overflowed
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
//     * @see #getGuessedMean()
     */
    public long getUncorrectedSum() {
        return sum;
    }

    public long getUncorrectedSumOfSquares() {
        return squareSum;
    }


    @Override
    public StatisticalLong multiply(double d) {
        sum *= d;

        squareSum = Math.multiplyExact(squareSum, (long) (d * d));
        guessedMean *= d;
        max = DoubleUtils.round(max * d);
        min = DoubleUtils.round(min * d);
        return this;
    }

    public StatisticalLong add(long d) {
        if (mode != Mode.LONG) {
            throw new IllegalStateException();
        }
        return _add(d);
    }

    protected StatisticalLong _add(long d) {
        reguess();
        long dcount = d * count;
        squareSum = addExact(squareSum, multiplyExact(d, addExact(dcount,  multiplyExact(2, sum))));
        sum += dcount;
        autoGuess = false;
        max = max + d;
        min = min + d;
        reguess();
        return this;
    }

    public StatisticalLong add(Duration d) {
        switch(mode) {
            case DURATION:
            case INSTANT:
                return _add(d.toMillis());
            case DURATION_NS:
                return _add(d.toNanos());
            default:
                throw new IllegalStateException();
        }
    }

    public StatisticalLong plus(Duration d) {
        return copy().add(d);
    }

    public StatisticalLong plus(long d) {
        return copy().add(d);
    }

    @Override
    public void accept(long value) {
        enter(value);
    }

    public void enter(Instant... instants) {
        if (mode != Mode.INSTANT) {
            throw new IllegalStateException();
        }
        for (Instant i : instants) {
            long d = i.toEpochMilli();
            accept(d);
        }
    }
    public void enter(Duration... duration) {
        if (! durationMode()) {
            throw new IllegalStateException();
        }
        for (Duration d : duration) {
            accept(mode == Mode.DURATION ? d.toMillis() : d.toNanos());
        }
    }

    protected boolean durationMode() {
        return mode == Mode.DURATION || mode == Mode.DURATION_NS;
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
    public StatisticalLong reguess() {
        final long newGuess = longValue();
        final long diff =  newGuess - guessedMean;
        this.squareSum = getSumOfSquares(diff);
        this.sum = sum - count * diff;
        this.guessedMean = newGuess;
        return this;
    }

}



