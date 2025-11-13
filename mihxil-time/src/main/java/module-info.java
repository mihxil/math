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
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.time.eventsearchers.EventSearcher;
import org.meeuw.time.eventsearchers.christian.ChristianHolidaySearcher;
import org.meeuw.time.eventsearchers.seasons.SeasonsEventSearcher;
import org.meeuw.time.eventsearchers.wellknown.us.WellknownHolidaySearcher;
import org.meeuw.time.text.TimeConfiguration;
import org.meeuw.time.text.spi.UncertainTemporalFormatProvider;
import org.meeuw.time.text.spi.ZoneIdToString;

/**
 * @author Michiel Meeuwissen
 * @since 0.18
 */
module org.meeuw.time {

    requires static lombok;
    requires static org.checkerframework.checker.qual;

    requires org.meeuw.math;
    requires org.meeuw.configuration;
    requires org.meeuw.functional;

    exports org.meeuw.time;
    exports org.meeuw.time.text.spi;
    exports org.meeuw.time.text;
    exports org.meeuw.time.parser;
    exports org.meeuw.time.eventsearchers;
    exports org.meeuw.time.eventsearchers.seasons to org.meeuw.time.test;
    exports org.meeuw.time.eventsearchers.wellknown.us  to org.meeuw.time.test;
    exports org.meeuw.time.eventsearchers.christian to org.meeuw.time.test;

    uses AlgebraicElementFormatProvider;
    uses EventSearcher;

    provides EventSearcher with
        SeasonsEventSearcher,
        ChristianHolidaySearcher,
        WellknownHolidaySearcher;

    provides AlgebraicElementFormatProvider with
        UncertainTemporalFormatProvider;

    provides ConfigurationAspect with
        TimeConfiguration;

    provides ToStringProvider with
        ZoneIdToString;


}
