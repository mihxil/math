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

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

/**
 * This configuration aspect defines how uncertainties must be formatted
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
public class UncertaintyConfiguration implements ConfigurationAspect {

    /**
     * Uncertainties can be formatted in several ways, each labeled with the appropriate {@link Notation} enum value.
     */
    @Getter
    @With
    private final Notation notation;

    @Getter
    @With
    private final double considerRoundingErrorFactor;

    @lombok.Builder
    private UncertaintyConfiguration(Notation notation,  double considerRoundingErrorFactor) {
        this.notation = notation;
        this.considerRoundingErrorFactor = considerRoundingErrorFactor;
    }

    public UncertaintyConfiguration() {
        this(Notation.PLUS_MINUS, 1000d);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(UncertainDoubleFormatProvider.class);
    }

    public enum Notation {
        /**
         * Use a ± symbol between value and uncertainty
         */
        PLUS_MINUS,
        /**
         * User parentheses to indicate the uncertainty in the last displayed decimals.
         */
        PARENTHESES,

        /**
         * Just round the value, indicating like that the uncertaintity
         *
         * @since 0.19
         */
        ROUND_VALUE,

        /**
         * Just round the value, stripping trailing zero's too
         *
         * @since 0.19
         */
        ROUND_VALUE_AND_TRIM
    }
}
