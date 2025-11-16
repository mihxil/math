package org.meeuw.time.eventsearchers.daysoftheweek;

import java.time.Instant;
import java.time.Year;
import java.util.stream.Stream;

import org.meeuw.time.Range;
import org.meeuw.time.eventsearchers.EventSearcher;
import org.meeuw.time.eventsearchers.seasons.Season;


/**
 * @since 0.19
 * @author Michiel Meeuwissen
 */
public class WeekDayEventSearch implements EventSearcher<Instant>
{

  @Override
  public Stream<Instant> findEvents(Range<Year> range, String description) {

      Season season = Season.fromDescription(description).orElse(null);
      if (season == null) {
          return Stream.empty();
      }
      return Range.stream(range).map(season);
  }
}
