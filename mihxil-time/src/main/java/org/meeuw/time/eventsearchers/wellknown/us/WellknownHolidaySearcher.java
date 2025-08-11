package org.meeuw.time.eventsearchers.wellknown.us;

import java.util.Optional;

import org.meeuw.time.eventsearchers.impl.AbstractYearlyHolidayEventSearcher;


/**
 * @since 0.19
 * @author Michiel Meeuwissen
 */
public class WellknownHolidaySearcher extends AbstractYearlyHolidayEventSearcher {


    public WellknownHolidaySearcher() {

    }

    @Override
    public Optional<WellknownHoliday> fromSummary(String eventSummary) {
        return WellknownHoliday.fromSummary(eventSummary);
    }
}
