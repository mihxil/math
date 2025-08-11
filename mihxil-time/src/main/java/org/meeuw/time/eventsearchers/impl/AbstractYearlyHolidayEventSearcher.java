package org.meeuw.time.eventsearchers.impl;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;
import java.util.stream.Stream;

import org.meeuw.time.*;
import org.meeuw.time.eventsearchers.EventSearcher;


public abstract class AbstractYearlyHolidayEventSearcher implements EventSearcher<LocalDate> {


  public abstract Optional<? extends YearlyEvent> fromSummary(String eventSummary);

  @Override
  public Stream<LocalDate> findEvents(Range<Year> range, String eventSummary) {
    YearlyEvent holiday = fromSummary(eventSummary).orElse(null);
    if (holiday == null) {
      return Stream.empty();
    }
     return Range.stream(range).map(holiday);

  }
}
