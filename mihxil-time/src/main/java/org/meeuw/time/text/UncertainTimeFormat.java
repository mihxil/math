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
package org.meeuw.time.text;

import lombok.Getter;
import lombok.Setter;

import java.text.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.time.*;

import static org.meeuw.math.text.UncertainNumberFormat.valuePlusMinError;
import static org.meeuw.time.TimeUtils.round;
import static org.meeuw.time.TimeUtils.roundStddev;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Setter
@Getter
public class UncertainTimeFormat extends Format {

    ZoneId zoneId = ZoneId.systemDefault();

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
         if (number instanceof UncertainJavaTime<?> uncertainJavaTime) {

             switch (uncertainJavaTime.getMode()) {
                 case INSTANT: {
                     toAppendTo.append(formatInstant(Instant.ofEpochMilli(uncertainJavaTime.getValue().longValue()),
                         Duration.ofMillis(uncertainJavaTime.getUncertainty().longValue())));
                     return toAppendTo;
                 }
                 case DURATION_NS:{
                     toAppendTo.append(formatDuration(
                         Duration.ofNanos(uncertainJavaTime.getValue().longValue()),
                         roundStddev(Duration.ofNanos(uncertainJavaTime.getUncertainty().longValue()))
                     ));
                     return toAppendTo;
                 }
                 case DURATION: {
                     toAppendTo.append(formatDuration(
                         Duration.ofMillis(uncertainJavaTime.getValue().longValue()),
                         roundStddev(Duration.ofMillis(uncertainJavaTime.getUncertainty().longValue()))
                     ));
                     return toAppendTo;
                 }
                 case LONG: {
                     throw new IllegalArgumentException();
                 }
                 default:
            }
         } else if (number instanceof UncertainInstant<?> uncertainInstant) {
             toAppendTo.append(formatInstant(uncertainInstant.instantValue(),
                 uncertainInstant.instantUncertainty()));
             return toAppendTo;
         } else if (number instanceof UncertainDuration<?> uncertainDuration) {
             toAppendTo.append(formatDuration(uncertainDuration.durationValue(),
                 uncertainDuration.durationUncertainty()));
             return toAppendTo;
         }
         throw new IllegalArgumentException("Cannot format given " + number.getClass() + " as a UncertainJavaTime");
    }

    protected String formatDuration(Duration duration, Duration stddev) {
        ChronoUnit order = TimeUtils.orderOfMagnitude(stddev);
        return valuePlusMinError(duration.toString(), round(stddev, order).toString());
    }
    protected String formatInstant(Instant instant, Duration stddev) {
        ChronoUnit order = TimeUtils.orderOfMagnitude(stddev);
        return valuePlusMinError(TimeUtils.format(zoneId, instant, order), round(stddev, order).toString());
    }




    @Override
    public Object parseObject(String source, @NonNull ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
