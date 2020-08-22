/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.statistics;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import org.meeuw.math.Utils;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.UncertainDoubleField;

/**
 * Keeps tracks the sum and sum of squares of a sequence of long values.
 *
 * @author Michiel Meeuwissen
 */
public class StatisticalLong extends StatisticalNumber<StatisticalLong> implements LongConsumer, IntConsumer {

    private long sum = 0;
    private long squareSum = 0;
    @Getter
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

    public StatisticalLong() {
        this.mode = Mode.LONG;
    }

    public StatisticalLong(Mode mode) {
        this.mode = mode;
    }

    protected StatisticalLong(Mode mode, long sum, long squareSum, int count, long guessedMean) {
        super(count);
        this.mode = mode == null ? Mode.LONG : mode;
        this.squareSum = squareSum;
        this.sum = sum;
        this.guessedMean = guessedMean;
    }

    @Override
    public StatisticalLong plus(double summand) {
        return plus(Math.round(summand));
    }
    @Override
    public StatisticalLong copy() {
        StatisticalLong c = new StatisticalLong(mode, sum, squareSum, count, guessedMean);
        c.max = max;
        c.min = min;
        c.autoGuess = autoGuess;
        return c;
    }

    /**
     * Enters new value(s).
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
            sum += d;
            squareSum += d * d;
            count++;

        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link StatisticalLong#plus(UncertainDouble)}} which is something entirely different.
     * @param m The other {@link StatisticalLong} which value must be ented into this one
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

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    protected long round(double in) {
        long orderOfMagnitude = Utils.positivePow10(Utils.log10(getStandardDeviation()));
        return Math.round(in) / orderOfMagnitude * orderOfMagnitude;
    }


    @Override
    public UncertainDoubleField getStructure() {
        return  UncertainDoubleField.INSTANCE;
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
        if (count == 0) {
            return Double.NaN;
        }
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
     * This implementation keeps track of a 'guessedMean' (see {@link #getGuessedMean()} value. The internal value {@link #getUncorrectedSumOfSquares()} is kept small like this, to avoid long overflows.
     *
     * Calculating the {@link #getStandardDeviation()} happens using these 'uncorrected' (but smaller) versions, because the value should be the same. The actual sum of squares of all values is given by {@link #getSumOfSquares()}, which is the calculated but may more easily overflow.
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
        squareSum *= d * d;
        guessedMean *= d;
        max = Math.round(max * d);
        min = Math.round(min * d);
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
        squareSum += d * (dcount + 2 * sum);
        sum += dcount;
        autoGuess = false;
        max = max + d;
        min = min + d;
        reguess();
        return this;
    }

    public StatisticalLong add(Duration d) {
        if (mode == Mode.LONG) {
            throw new IllegalStateException();
        }
        return _add(d.toMillis());
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
         if (mode != Mode.DURATION) {
            throw new IllegalStateException();
        }
        for (Duration d : duration) {
            accept(d.toMillis());
        }
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
     */
    public StatisticalLong reguess() {
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



