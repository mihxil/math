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
package org.meeuw.math.abstractalgebra.rationalnumbers.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.rationalnumbers.text.spi.RationalNumberFormatProvider;

/**
 * The configuration aspect which specifies how numbers should be formatted.
 *
 * @author Michiel Meeuwissen
 * @since 0.20
 */
@EqualsAndHashCode
public class RationalNumberConfiguration implements ConfigurationAspect {


    @Getter
    Mode mode = Mode.FRACTION;


    @lombok.Builder
    private RationalNumberConfiguration(
        Mode mode
       ) {
        this.mode = mode;
    }

    public RationalNumberConfiguration() {
        this(Mode.FRACTION);
    }



    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(RationalNumberFormatProvider.class);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RationalNumberConfiguration.class.getSimpleName() + "(", ")")
            .add("mode=" + mode)
            .toString();
    }

    public enum Mode {
        /**
         * Show as one fraction of two big integers
         */
        FRACTION,
        /**
         * Show as an integer and a faction of 2 big integers (which is smaller than 1)
         */
        INTEGER_AND_FRACTION,
        /**
         * Shows as a decimal number, with repeated digits indicated with overline
         */
        DECIMAL_WITH_REPEAT,
        /**
         * Shows as a normal decimal number. Rounded to some precision.
         */
        DECIMAL_ROUNDED
    }
}
