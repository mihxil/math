package org.meeuw.time.eventsearchers.wellknown;

import java.time.*;
import java.util.*;
import java.util.function.Function;

import static org.meeuw.time.eventsearchers.wellknown.WellknownIrregularHolidaySearcher.SPECIAL_INAUGURATIONS;


/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public enum WellknownIrregularHoliday implements Function<Year, List<LocalDate>>{



  /** First Tuesday after the first Monday in November, US general elections. */
  ELECTION_DAY("US General Election", (year) -> {
    LocalDate firstDayOfNovember = LocalDate.of(year.getValue(), 11, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfNovember.getDayOfWeek().getValue() + 7) % 7 + 1;
    return Collections.singletonList(firstDayOfNovember.plusDays(daysToAdd));
  }),

  INAUGURATION_DAY("Inauguration Day", (year) ->  {
    int y = year.getValue();
    if (y < 1789) return Collections.emptyList();



    List<LocalDate> d = SPECIAL_INAUGURATIONS.get(y);
    if (d != null) {
      return d;
    }
    List<LocalDate> result = new ArrayList<>();
    // Regular inaugurations from 1937 onward (every 4 years)
    if (y >= 1937 && (y - 1937) % 4 == 0) {
      LocalDate jan20 = LocalDate.of(y, 1, 20);
      if (jan20.getDayOfWeek() == DayOfWeek.SUNDAY) {
        result.add(jan20.plusDays(1));
      } else {
        result.add(jan20);
      }
    }
    return result;
  })
  ;

  private final String summary;
  final Function<Year, List<LocalDate>> dateFunction;

  private static final Map<String, WellknownIrregularHoliday> lookup;

  static {
    Map<String, WellknownIrregularHoliday> map = new HashMap<>();
    for (WellknownIrregularHoliday h : values()) {
      map.put(h.getSummary().toLowerCase(), h);
    }
    lookup = Collections.unmodifiableMap(map);
  }

  WellknownIrregularHoliday(String summary, Function<Year, List<LocalDate>> dateFunction) {
    this.summary = summary;
    this.dateFunction = dateFunction;
  }

  public String getSummary() {
    return summary;
  }

  public static Optional<WellknownIrregularHoliday> fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(lookup.get(summary.toLowerCase()));
  }


  @Override
  public List<LocalDate> apply(Year year) {
    return dateFunction.apply(year);
  }


}
