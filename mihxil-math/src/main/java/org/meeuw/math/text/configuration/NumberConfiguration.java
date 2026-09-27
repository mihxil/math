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
package org.meeuw.math.text.configuration;

import lombok.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.numbers.DecimalFormatToString;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;
import org.meeuw.math.text.spi.UncertainNumberFormatProvider;

import static org.meeuw.math.text.configuration.GroupingSeparator.NONE;

/**
 * The configuration aspect which specifies how numbers should be formatted.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class NumberConfiguration implements ConfigurationAspect {

    private static final DecimalFormat DEFAULT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
    static {
        DEFAULT.getDecimalFormatSymbols().setInfinity(TextUtils.INFINITY);
        DEFAULT.setGroupingUsed(false);
    }

    public static DecimalFormat getDefaultNumberFormat() {
        return (DecimalFormat) DEFAULT.clone();
    }

    /**
     * If the absolute value of the exponent is bigger than this, then
     * scientific notation will be used. Otherwise, no.
     * <p>
     * This defaults to 4.
     */
    @Getter
    @With
    private final int minimalExponent;

    @Getter
    @With
    private final GroupingSeparator groupingSeparator;

    @Getter
    @With
    private final DecimalFormat decimalFormat;

    @Getter
    @With
    private final int maximalPrecision;



    @lombok.Builder
    private NumberConfiguration(
        int minimalExponent,
        GroupingSeparator groupingSeparator,
        DecimalFormat decimalFormat,
        int maximalPrecision
       ) {
        this.minimalExponent = minimalExponent;
        this.decimalFormat = (DecimalFormat) decimalFormat.clone();
        this.groupingSeparator = groupingSeparator;
        this.maximalPrecision = maximalPrecision;
        this.decimalFormat.setGroupingUsed(groupingSeparator != NONE);
        this.decimalFormat.setMaximumFractionDigits(maximalPrecision);
    }

    public NumberConfiguration() {
        this(4, NONE, getDefaultNumberFormat(), 1000);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return List.of(UncertainDoubleFormatProvider.class, UncertainNumberFormatProvider.class);
    }

    @Override
    public String toString() {
        String format = new DecimalFormatToString().toString(this.decimalFormat)
            .orElseGet(() -> "DecimalFormat(maximumFractionDigits=" + decimalFormat.getMaximumFractionDigits() + ")");
        return new StringJoiner(", ", NumberConfiguration.class.getSimpleName() + "(", ")")
            .add("minimalExponent=" + minimalExponent)
            .add("numberFormat=" + format)
            .toString();
    }
}
