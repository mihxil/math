package org.meeuw.test.time.eventsearchers.wellknown;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.time.Range;
import org.meeuw.time.eventsearchers.EventSearcherService;
import org.meeuw.time.eventsearchers.wellknown.us.WellknownIrregularHoliday;
import org.meeuw.time.eventsearchers.wellknown.us.WellknownIrregularHolidaySearcher;


public class WellknownIrregularHolidaySearcherTest  {


    @Test
    public void inaugurations() {

        for (int year = 1750; year <= 2080; year++) {
            List<LocalDate> apply = WellknownIrregularHoliday.INAUGURATION_DAY.apply(Year.of(year));
            if (apply.isEmpty()) {
                continue;
            }
            System.out.println("inauguration: " + apply);
        }

    }


    @Test
    public void searcher() {
        WellknownIrregularHolidaySearcher searcher = new WellknownIrregularHolidaySearcher();

        searcher.findEvents(Range.ofYears(1750, 2080), "inauguration day")
            .forEach(System.out::println);


    }

    @Test
    public void service() {


        EventSearcherService.INSTANCE.findEvents(Range.ofYears(2025, 1750), "inauguration day")
            .forEach(System.out::println);


  }

}
