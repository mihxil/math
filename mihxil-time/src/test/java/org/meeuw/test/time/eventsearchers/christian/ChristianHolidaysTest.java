package org.meeuw.test.time.eventsearchers.christian;

import lombok.extern.java.Log;

import java.time.Year;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.meeuw.time.eventsearchers.christian.ChristianHoliday;


@Log
public class ChristianHolidaysTest  {

  @Test
  public void testChristianHolidays() {

    Stream.of(ChristianHoliday.values()).forEach(holiday -> {
      log.info(holiday.getDescription() + " on " + holiday.apply(Year.of(2025)));
    });
  }

}
