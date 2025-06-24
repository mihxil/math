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
import java.time.temporal.*;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.statistics.time.*;
import org.meeuw.math.time.BasicUncertainDuration;
import org.meeuw.math.time.UncertainDuration;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.statistics.time.UncertainJavaTime.Mode.LONG;

/**
 * Keeps tracks the sum and sum of squares of a sequence of long values.
 * <p>
 * It can work in different {@link Mode}s, which indicates how the long value itself must be interpreted.
 * Therefore, this implements {@link TemporalAmount} and {@link Temporal}, but related methods only work in the corresponding mode.
 * This may get  deprecated, though. And in preparation this is now implementing {@link StatisticalInstant}, {@link StatisticalDuration} if the value must be interpreted as such.
 * <p>
 *
 *
 * @author Michiel Meeuwissen
 */
@Log
public class StatisticalLong extends AbstractStatisticalLong<StatisticalLong> implements
    StatisticalInstant<StatisticalLong, Double, UncertainReal>,
    StatisticalDuration<StatisticalLong, Double, UncertainReal>,
    UncertainJavaTime<Double> {

    @Getter
    @NonNull
    private final Mode mode;


    public StatisticalLong() {
        this(null);
    }

    //@Deprecated
    public StatisticalLong(@Nullable Mode mode) {
        this.mode = mode == null ? LONG : mode;
    }

    protected StatisticalLong(@NonNull Mode mode, long sum, long squareSum, int count, long guessedMean) {
        super(sum, squareSum, count, guessedMean);
        this.mode = mode;
    }

    @Override
    public StatisticalLong plus(double summand) {
        long rounded = DoubleUtils.round(summand);
        StatisticalLong result = plus(rounded);
        result.doubleOffset = (summand - (double) rounded);
        return result;
    }


    @Override
    protected StatisticalLong _copy() {
        StatisticalLong c = new StatisticalLong(mode, sum, squareSum, count, guessedMean);
        return c;
    }



    @Override
    public Temporal addTo(Temporal temporal) {
        if (mode != Mode.DURATION && mode != Mode.DURATION_NS) {
            throw new IllegalStateException();
        }
        if (temporal instanceof StatisticalLong) {
            StatisticalLong statisticalLong = (StatisticalLong) temporal;
            return statisticalLong.plus(durationValue());
        }
        return durationValue().addTo(temporal);
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        if (mode != Mode.DURATION && mode != Mode.DURATION_NS) {
            throw new IllegalStateException();
        }
        return durationValue().subtractFrom(temporal);
    }

    @Override
    public Temporal plus(long amountToAdd, TemporalUnit unit) {
        return instantValue().plus(amountToAdd, unit);
    }

    @Override
    public UncertainDuration<Double> until(Temporal endExclusive) {
        if (endExclusive instanceof UncertainDuration<?>) {
            UncertainDuration<?> uncertainDuration = (UncertainDuration<?>) endExclusive;
            return plus(uncertainDuration.durationValue());
        }
        long result = instantValue().until(endExclusive, ChronoUnit.MILLIS);
        return new BasicUncertainDuration<>((double) result, getUncertainty());

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
            .map(bd ->
                switch (mode) {
                    case DURATION -> Duration
                        .ofMillis(bd.longValue())
                        .plusNanos(
                            bd.remainder(BigDecimal.ONE).multiply(BIG_NANOS_IN_MILLIS).longValue()
                        );
                    case DURATION_NS -> Duration.ofNanos(bd.longValue());
                    default -> throw new IllegalStateException();
                }
            );
    }

    public StatisticalLong add(long d) {
        if (mode != LONG) {
            throw new IllegalStateException();
        }
        return super.add(d);
    }

    private StatisticalLong _add(TemporalAmount d) {
        return switch (mode) {
            case DURATION, INSTANT -> _add(toMillis(d));
            case DURATION_NS -> _add(toNanos(d));
            default -> throw new IllegalStateException();
        };
    }

    private static long toMillis(TemporalAmount d) {
        long millis = 0;
        for (TemporalUnit unit : d.getUnits()) {
            millis += unit.getDuration().toMillis() * d.get(unit);
        }
        return millis;
    }
    private static long toNanos(TemporalAmount d) {
        long millis = 0;
        for (TemporalUnit unit : d.getUnits()) {
            millis += unit.getDuration().toNanos() * d.get(unit);
        }
        return millis;
    }

    public StatisticalLong add(Duration d) {
        return _add(d);
    }


    @Override
    public StatisticalLong plus(TemporalAmount d) {
        return copy()._add(d);
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

}



