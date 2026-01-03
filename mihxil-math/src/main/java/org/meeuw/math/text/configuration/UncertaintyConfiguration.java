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
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * This configuration aspect defines how uncertainties must be formatted
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
@Log
public class UncertaintyConfiguration implements ConfigurationAspect {

    public static BiPredicate<Notation, Object> DEFAULT_STRIP_ZEROS = new BiPredicate<Notation, Object>() {
        @Override
        public boolean test(Notation notation, Object object) {
            if (object instanceof UncertainNumber<?> number) {
                boolean strip = notation == Notation.ROUND_VALUE && number.isExact();
                log.info(() ->  number.getValue() +  " -> " + strip);
                return strip;
            } else {
                return false;
            }
        }
        @Override
        public String toString() {
            return "if " + Notation.ROUND_VALUE + " and exact";
        }
    };

    /**
     * Uncertainties can be formatted in several ways, each labeled with the appropriate {@link Notation} enum value.
     */
    @Getter
    @With
    private final Notation notation;

    @Getter
    @With
    private final double considerRoundingErrorFactor;

    @Getter
    @With
    private final BiPredicate<Notation, Object> stripZeros;


    @lombok.Builder
    private UncertaintyConfiguration(
        Notation notation,
        double considerRoundingErrorFactor,
        BiPredicate<Notation, Object> stripZeros
        ) {
        this.notation = notation;
        this.considerRoundingErrorFactor = considerRoundingErrorFactor;
        this.stripZeros = stripZeros == null ?
            DEFAULT_STRIP_ZEROS : stripZeros;
    }

    public UncertaintyConfiguration() {
        this(Notation.PLUS_MINUS, 1000d, null);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(UncertainDoubleFormatProvider.class);
    }

    public enum Notation {
        /**
         * Use a ± symbol between value and uncertainty
         */
        PLUS_MINUS(true),


        /**
         * User parentheses to indicate the uncertainty in the last displayed decimals.
         */
        PARENTHESES(false),

        /**
         * Just round the value, indicating like that the uncertainty
         *
         * @since 0.19
         */
        ROUND_VALUE(false);


        private final boolean useBrackets;

        Notation(boolean useBrackets) {
            this.useBrackets = useBrackets;
        }

        public boolean useBrackets() {
            return useBrackets;
        }
    }
}
