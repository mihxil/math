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

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.spi.ToStringProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math {

    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires static ch.obermuhlner.math.big;
    requires static jakarta.validation;

    requires java.logging;
    requires org.meeuw.configuration;
    requires org.meeuw.functional;

    exports org.meeuw.math;
    exports org.meeuw.math.abstractalgebra;
    exports org.meeuw.math.text;
    exports org.meeuw.math.uncertainnumbers;
    exports org.meeuw.math.uncertainnumbers.field;
    exports org.meeuw.math.exceptions;

    exports org.meeuw.math.text.spi;
    exports org.meeuw.math.text.configuration;
    exports org.meeuw.math.numbers;
    exports org.meeuw.math.streams;
    exports org.meeuw.math.operators;
    exports org.meeuw.math.validation;
    exports org.meeuw.math.abstractalgebra.product;
    exports org.meeuw.math.abstractalgebra.trivial;
    exports org.meeuw.math.abstractalgebra.categoryofgroups;
    exports org.meeuw.math.time;

    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;

    provides AlgebraicElementFormatProvider with
        UncertainDoubleFormatProvider,
        UncertainNumberFormatProvider
        ;
    provides ConfigurationAspect with
        StreamUtils.Configuration,
        NumberConfiguration,
        UncertaintyConfiguration,
        MathContextConfiguration,
        RandomConfiguration,
        GenericGroupConfiguration,
        ConfidenceIntervalConfiguration,
        CompareConfiguration
        ;
    provides ToStringProvider with
        DecimalFormatToString,
        MathContextToString
        ;

}

