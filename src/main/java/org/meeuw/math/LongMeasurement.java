/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * Keeps tracks the sum and sum of squares of a sequence of long values.
 *
 * These
 *
 * @author Michiel Meeuwissen
 */
public class LongMeasurement extends MeasurementNumber<LongMeasurement> implements LongConsumer, IntConsumer {

    private long sum = 0;
    private long squareSum = 0;
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

    public LongMeasurement() {
        this.mode = Mode.LONG;
    }
    public LongMeasurement(Mode mode) {
        this.mode = mode;
    }

    protected LongMeasurement(Mode mode, long sum, long squareSum, int count, long guessedMean) {
        super(count);
        this.mode = mode == null ? Mode.LONG : mode;
        this.squareSum = squareSum;
        this.sum = sum;
        this.guessedMean = guessedMean;
    }

    public LongMeasurement copy() {
        LongMeasurement c = new LongMeasurement(mode, sum, squareSum, count, guessedMean);
        c.max = max;
        c.min = min;
        c.autoGuess = autoGuess;
        return c;
    }

    /**
     * Enters new value(s).
     */
    public LongMeasurement enter(long... ds) {
        for(long d : ds) {
            if (autoGuess) {
                guessedMean = d;
                autoGuess = false;
            }
            min = Math.min(min, d);
            max = Math.max(max, d);
            d -= guessedMean;
            sum += d;
            squareSum += d * d;
            count++;

        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link #plus(LongMeasurement)} which is something entirely different.
     * @param m
     */
    @Override
    public LongMeasurement enter(LongMeasurement m) {
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

    public double getMean() {
        return (double) guessedMean + ((double) sum / count);
    }

    public long getRoundedMean() {
        return round(getMean());
    }

    @Override
    public void accept(int value) {
        enter(value);
    }

    protected long round(double in) {
        long orderOfMagnitude = Utils.positivePow10(Utils.log10(getStandardDeviation()));
        return Math.round(in) / orderOfMagnitude * orderOfMagnitude;
    }


    @Override
    public double doubleValue() {
        return getMean();
    }

    public Instant instantValue() {
        return Instant.ofEpochMilli(longValue());
    }

    public Duration durationValue() {
        return Duration.ofMillis(longValue());
    }

    @Override
    public double getStandardDeviation() {
        double mean = ((double) sum) / count;
        return Math.sqrt((double) (squareSum / count) - mean * mean);
    }


    public long getSum() {
        return sum + count * guessedMean;
    }

    public long getSumOfSquares() {
        return getSumOfSquares(-1 * guessedMean);
    }

    protected long getSumOfSquares(long offset) {
        return squareSum - 2 * offset * sum + count * (offset * offset);
    }


    /**
     * This implementation keeps track of a 'guessedMean' (see {@link #getGuessedMean()} value. The internal value {@link #getUncorrectedSumOfSquares()}it kept small like this, to avoid long overflows.
     *
     * Calculating the {@link #getStandardDeviation()} happens using these 'uncorrected' (but smaller) versions, because the value should be the same. The actual sum of squares of all values is given by {@link #getSumOfSquares()}, which is the calculated but may more easily overflow.
     */
    public long getUncorrectedSum() {
        return sum;
    }

    public long getUncorrectedSumOfSquares() {
        return squareSum;
    }

    /**
     * Operator overloading would be very handy here, but java sucks.
     */
    @Override
    public LongMeasurement div(double d) {
        return times(1 / d);
    }
    @Override
    public LongMeasurement times(double d) {
        LongMeasurement m = new LongMeasurement(mode, Math.round((double) sum * d), Math.round((double) squareSum * (d * d)), count,  (long) (guessedMean * d));
        m.autoGuess = false;
        m.max = Math.round(max * d);
        m.min = Math.round(min * d);
        return m;
    }

    public LongMeasurement plus(long d) {
        if (mode != Mode.LONG) {
            throw new IllegalStateException();
        }
        return _plus(d);
    }

    protected LongMeasurement _plus(long d) {
        LongMeasurement m = copy().reguess();
        m.sum += d * count;
        m.squareSum += d * d * count + 2 * sum * d;
        m.autoGuess = false;
        m.max = max + d;
        m.min = min + d;
        m.reguess();
        return m;
    }

    public LongMeasurement plus(Duration d) {
        if (mode == Mode.LONG) {
            throw new IllegalStateException();
        }
        return _plus(d.toMillis());
    }

    /**
     * Assuming that this measurement is from a different set (the mean is <em>principally
     * different</em>)
     *
     */
    public LongMeasurement plus(LongMeasurement m) {
        // think about this...
        return new LongMeasurement(mode, m.count * sum + count + m.sum, /* er */ 0, count * m.count, 0);
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
         if (mode != Mode.DURATION) {
            throw new IllegalStateException();
        }
        for (Duration d : duration) {
            accept(d.toMillis());
        }
    }

    @Override
    public String toString() {
        switch(mode) {
            case INSTANT: {
                long rounded = round(getMean());
                Duration stddev = Duration.ofMillis((long) getStandardDeviation());
                return Utils.valueAndError(Instant.ofEpochMilli(rounded).toString(), stddev.toString());
            }
            case DURATION: {
                long rounded = Math.round(getMean());
                Duration stddev = Duration.ofMillis((long) getStandardDeviation());
                return Utils.valueAndError(Duration.ofMillis(rounded).toString(), stddev.toString());
            }
            default:
            case LONG: return super.toString();
        }
    }


    @Override
    public void reset() {
        super.reset();
        sum = 0;
        squareSum = 0;
        autoGuess = true;
    }


    /**
     * Uses the current {@link #getMean()} value as a new offset for values when keeping track of the sum and sum of squares of the values.
     */
    public LongMeasurement reguess() {
        long newGuess = longValue();
        long diff =  newGuess - guessedMean;
        this.squareSum = getSumOfSquares(diff);
        this.sum = sum - count * diff;
        this.guessedMean = newGuess;
        return this;
    }

    public enum Mode {
        LONG,
        INSTANT,
        DURATION
    }
}



