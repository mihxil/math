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

import java.text.*;
import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.numbers.DecimalFormatToString;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

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

    }
    static {
        DEFAULT.setGroupingUsed(false);
    }

    public static DecimalFormat getDefaultNumberFormat() {
        return (DecimalFormat) DEFAULT.clone();
    }

    /**
     * If the absolute value of the exponent would be bigger than this, then
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
    private final DecimalFormat numberFormat;


    @Getter
    @With
    private final int maximalPrecision;



    @lombok.Builder
    private NumberConfiguration(
        int minimalExponent,
        GroupingSeparator groupingSeparator,
        DecimalFormat numberFormat,
        int maximalPrecision
       ) {
        this.minimalExponent = minimalExponent;
        this.numberFormat = numberFormat;
        this.groupingSeparator = groupingSeparator;
        this.maximalPrecision = maximalPrecision;
    }

    public NumberConfiguration() {
        this(4, NONE, getDefaultNumberFormat(), Integer.MAX_VALUE);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(UncertainDoubleFormatProvider.class);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NumberConfiguration.class.getSimpleName() + "(", ")")
            .add("minimalExponent=" + minimalExponent)
            .add("numberFormat=" + new DecimalFormatToString().toString(this.numberFormat).orElse(numberFormat.toString()))
            .toString();
    }
}
