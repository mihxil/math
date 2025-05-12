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
import org.meeuw.math.statistics.text.spi.UncertainTemporalFormatProvider;
import org.meeuw.math.statistics.text.spi.ZoneIdToString;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.statistics {
    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires static jakarta.validation;
    requires static jakarta.annotation;

    requires java.logging;
    requires org.meeuw.math;
    requires org.meeuw.configuration;

    exports org.meeuw.math.statistics;
    exports org.meeuw.math.statistics.text;
    exports org.meeuw.math.windowed;
    exports org.meeuw.math.statistics.time;

    uses AlgebraicElementFormatProvider;
    uses ConfigurationAspect;


    provides AlgebraicElementFormatProvider with
        UncertainTemporalFormatProvider;

    provides ConfigurationAspect with
        TimeConfiguration;

    provides ToStringProvider with
        ZoneIdToString;


}

