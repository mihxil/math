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
import org.meeuw.math.*;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.temporal.UncertainTemporal;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.text.UncertainNumberFormat.valuePlusMinError;
import org.meeuw.math.time.TimeUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainTemporalFormat extends Format {

    @Getter
    @Setter
    ZoneId zoneId = ZoneId.systemDefault();

    @SuppressWarnings("rawtypes")
    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
         if (number instanceof StatisticalLong) {
             UncertainTemporal<?> statisticalLong = (UncertainTemporal) number;
             switch (statisticalLong.getMode()) {
                 case INSTANT: {
                     Instant mean = Instant.ofEpochMilli(statisticalLong.getValue().longValue());
                     Duration stddev = Duration.ofMillis((long) statisticalLong.getUncertainty().longValue());
                     ChronoUnit order = TimeUtils.orderOfMagnitude(stddev);
                     stddev = TimeUtils.round(stddev, order);
                     toAppendTo.append(valuePlusMinError(TextUtils.format(zoneId, mean, order), stddev.toString()));
                     return toAppendTo;
                 }
                 case DURATION: {
                     long rounded = DoubleUtils.round(statisticalLong.getValue().longValue());
                     Duration stddev = Duration.ofMillis((long) statisticalLong.getUncertainty().longValue());
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
