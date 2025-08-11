package org.meeuw.test.time.eventsearchers.christian;

import java.time.Year;
import java.util.stream.Stream;


import org.junit.jupiter.api.Test;

import org.meeuw.time.eventsearchers.christian.ChristianHoliday;


public class ChristianHolidaysTest  {

  @Test
  public void testChristianHolidays() {

    Stream.of(ChristianHoliday.values()).forEach(holiday -> {
      System.out.println(holiday.getDescription() + " on " + holiday.apply(Year.of(2025)));
    });
  }

}
