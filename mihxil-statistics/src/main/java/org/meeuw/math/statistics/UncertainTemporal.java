package org.meeuw.math.statistics;

import org.meeuw.math.uncertainnumbers.UncertainNumber;

public interface UncertainTemporal<N extends Number> extends UncertainNumber<N> {

    Mode getMode();


    /**
     * The long value contained in a {@link StatisticalLong} can be interpreted in different ways.
     */
    enum Mode {
        /**
         * Just a some long number.
         */
        LONG,

        /**
         * The long must be interpreted as a point in time. Milliseconds since EPOCH
         */
        INSTANT,

        /**
         * The long must be interpreted as duration. A number of milliseconds. This is probably precise enough for must cases.
         * For very short times, or more accuracy use {@link #DURATION_NS}, and durations will be stored as nanoseconds.
         */
        DURATION,


        /**
         * The long must be interpreted as duration. A number of nanos. Don't use this if durations vary more than a few seconds, since the sum of squares may rapidly go over {@link Long#MAX_VALUE} then.
         */
        DURATION_NS;

    }
}
