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
package org.meeuw.math.statistics.text;

import lombok.Getter;
import lombok.Setter;

import java.text.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.statistics.time.UncertainJavaTime;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.time.TimeUtils;

import static org.meeuw.math.text.UncertainNumberFormat.valuePlusMinError;
import static org.meeuw.math.time.TimeUtils.round;
import static org.meeuw.math.time.TimeUtils.roundStddev;

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
         if (number instanceof UncertainJavaTime<?> statisticalLong) {
             switch (statisticalLong.getMode()) {
                 case INSTANT: {
                     Instant mean = Instant.ofEpochMilli(statisticalLong.getValue().longValue());
                     Duration stddev = Duration.ofMillis(statisticalLong.getUncertainty().longValue());
                     ChronoUnit order = TimeUtils.orderOfMagnitude(stddev);
                     toAppendTo.append(valuePlusMinError(TextUtils.format(zoneId, mean, order), round(stddev, order).toString()));
                     return toAppendTo;
                 }
                 case DURATION_NS:{
                     long rounded = statisticalLong.getValue().longValue();
                     Duration stddev = roundStddev(Duration.ofNanos(statisticalLong.getUncertainty().longValue()));
                     toAppendTo.append(valuePlusMinError(Duration.ofNanos(rounded).toString(), stddev.toString()));
                     return toAppendTo;
                 }
                 case DURATION: {
                     long rounded = statisticalLong.getValue().longValue();
                     Duration stddev = roundStddev(Duration.ofMillis(statisticalLong.getUncertainty().longValue()));
                     toAppendTo.append(valuePlusMinError(Duration.ofMillis(rounded).toString(), stddev.toString()));
                     return toAppendTo;
                 }
                 case LONG: {
                     throw new IllegalArgumentException();
                 }
                 default:
            }
         }

         throw new IllegalArgumentException("Cannot format given " + number.getClass() + " as a StatisticalLong");
    }



    @Override
    public Object parseObject(String source, @NonNull ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
