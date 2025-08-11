package org.meeuw.time.eventsearchers.wellknown;

import java.util.Optional;

import org.meeuw.time.eventsearchers.AbstractYearlyHolidayEventSearcher;


/**
 * (Western) Christian holidays, such as Easter, Christmas, and Pentecost.
 * @since 1.1
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
