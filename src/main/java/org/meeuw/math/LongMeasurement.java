/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

import java.time.Duration;
import java.time.Instant;
import java.util.function.LongConsumer;

/**

 *
 * @author Michiel Meeuwissen
 */
public class LongMeasurement extends MeasurementNumber<LongMeasurement> implements LongConsumer {

    private long sum = 0;
    private long squareSum = 0;
    private final Mode mode;
    private long instantApprox;

    public LongMeasurement() {
        this.mode = Mode.LONG;
    }
    public LongMeasurement(Mode mode) {
        this.mode = mode;
    }
    protected LongMeasurement(Mode mode, long sum, long squareSum, int count) {
        super(count);
        this.mode = mode;
        this.squareSum = squareSum;
        this.sum = sum;
    }

    /**
     * Enters new value(s).
     */
    public LongMeasurement enter(long... ds) {
        for(long d : ds) {

            sum += d;
            squareSum += d * d;
            count++;
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link #add(LongMeasurement)} which is something entirely different.
     * @param m
     */
    @Override
    public LongMeasurement enter(LongMeasurement m) {
        sum += m.sum;
        squareSum += m.squareSum;
        count += m.count;
        return this;
    }

    public long getMean() {
        return sum / count;
    }
    public long getRoundedMean() {
        return round(getMean());
    }

    protected long round(long in) {
        long orderOfMagnitude = Utils.positivePow10(Utils.log10(getStandardDeviation()));
        return (in / orderOfMagnitude) * orderOfMagnitude;
    }


    @Override
    public double doubleValue() {
        return getMean();
    }

    @Override
    public double getStandardDeviation() {
        double mean = doubleValue();
        return Math.sqrt((double) (squareSum / count) - mean * mean);
    }


    public long getSum() {
        return sum;
    }
    public long getSumOfSquares() {
        return squareSum;
    }

    /**
     * Operator overloading would be very handy here, but java sucks.
     */
    @Override
    public LongMeasurement div(double d) {
        return new LongMeasurement(mode, (long) (sum / d), (long) (squareSum / (d * d)), count);
    }
    @Override
    public LongMeasurement times(double d) {
        return new LongMeasurement(mode, (long) (sum * d), (long) (squareSum * (d * d)), count);
    }

    public LongMeasurement add(long d) {
        return new LongMeasurement(mode, sum + d * count, squareSum + d * d * count + 2 * sum * d, count);
    }

    public LongMeasurement add(Duration d) {
        return add(d.toMillis());
    }

    /**
     * Assuming that this measurement is from a different set (the mean is <em>principally
     * different</em>)
     * @todo Not yet correctly implemented
     */
    public LongMeasurement add(LongMeasurement m) {
        // think about this...
        return new LongMeasurement(mode, m.count * sum + count + m.sum, /* er */ 0, count * m.count);
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
            if (instantApprox == 0) {
                instantApprox = d;
            }
            d -= instantApprox;
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
            case INSTANT:
                long rounded = round(getMean() + instantApprox);
                return Instant.ofEpochMilli(rounded).toString();
            default:
                case LONG: return super.toString();
        }
    }


    enum Mode {
        LONG,
        INSTANT,
        DURATION
    }
}



