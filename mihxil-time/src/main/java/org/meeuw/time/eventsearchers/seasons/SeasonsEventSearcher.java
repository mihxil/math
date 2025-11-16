package org.meeuw.time.eventsearchers.seasons;

import java.time.Instant;
import java.time.Year;
import java.util.stream.Stream;

import org.meeuw.time.*;
import org.meeuw.time.eventsearchers.EventSearcher;


/**
 * @since 0.19
 * @author Michiel Meeuwissen
 */
public class SeasonsEventSearcher implements EventSearcher<Instant> {


  @Override
  public Stream<Instant> findEvents(Range<Year> range, String description) {
      Season season = Season.fromDescription(description).orElse(null);
      if (season == null) {
          return Stream.empty();
      }
      return Range.stream(range).map(season);
  }
}
