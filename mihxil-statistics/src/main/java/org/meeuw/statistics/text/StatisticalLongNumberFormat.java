package org.meeuw.statistics.text;

import lombok.*;

import java.text.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

import org.meeuw.math.Utils;
import org.meeuw.math.text.TextUtils;
import org.meeuw.statistics.StatisticalLong;

import static org.meeuw.math.text.UncertainDoubleFormat.valuePlusMinError;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StatisticalLongNumberFormat extends Format {

    @Getter
    @Setter
    ZoneId zoneId = ZoneId.systemDefault();

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
         if (number instanceof StatisticalLong) {
             StatisticalLong statisticalLong = (StatisticalLong) number;
             switch (statisticalLong.getMode()) {
                 case INSTANT: {
                     Instant mean = Instant.ofEpochMilli(statisticalLong.longValue());
                     Duration stddev = Duration.ofMillis((long) statisticalLong.getStandardDeviation());
                     ChronoUnit order = Utils.orderOfMagnitude(stddev);
                     stddev = Utils.round(stddev, order);
                     toAppendTo.append(valuePlusMinError(TextUtils.format(zoneId, mean, order), stddev.toString()));
                     return toAppendTo;
                 }
                 case DURATION: {
                     long rounded = Math.round(statisticalLong.getMean());
                     Duration stddev = Duration.ofMillis((long) statisticalLong.getStandardDeviation());
                     toAppendTo.append(valuePlusMinError(Duration.ofMillis(rounded).toString(), stddev.toString()));
                     return toAppendTo;
                 }
                 default:
            }
         }

         throw new IllegalArgumentException("Cannot format given " + number.getClass() + " as a StatisticalLong");
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
