package org.meeuw.test.time.eventsearchers.seasons;

import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.meeuw.time.Range;
import org.meeuw.time.eventsearchers.EventSearcherService;
import org.meeuw.time.eventsearchers.seasons.Season;
import org.meeuw.time.eventsearchers.seasons.SeasonsEventSearcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.meeuw.time.eventsearchers.seasons.Season.*;


public class SeasonsTest {

    static final Map<Integer, Map<Season, String>> EXPECTED;
    static {
        Map<Season, String> seasons2025 = new HashMap<>();
        seasons2025.put(SPRING, "2025-03-20T09:00:42Z");
        seasons2025.put(SUMMER, "2025-06-21T02:31:08Z");
        seasons2025.put(FALL,   "2025-09-22T18:22:23Z");
        seasons2025.put(WINTER, "2025-12-21T15:01:41Z");

        Map<Integer, Map<Season, String>> expected = new HashMap<>();
        expected.put(2025, Collections.unmodifiableMap(seasons2025));

        EXPECTED = Collections.unmodifiableMap(expected);
    }
    @Test
    public void seasons() {

        Stream.of(Season.values()).forEach(season -> {
            assertThat( season.apply(Year.of(2025)).toString()).isEqualTo(EXPECTED.get(season));
        });
    }

    @Test
    public void service() {
        SeasonsEventSearcher seasonsEventSearcher = EventSearcherService.INSTANCE.getEventSearcher(SeasonsEventSearcher.class);
        seasonsEventSearcher.findEvents(Range.ofYears(1900, 2100), SPRING.getDescription())
            .forEach(event -> {
                System.out.println(SPRING + ": " +event);
                int year = event.atZone(ZoneId.of("UTC")).getYear();
                Map<Season, String> expected = EXPECTED.get(year);
                if (expected != null) {
                    assertEquals(expected.get(SPRING), event.toString());
                }
            });

    }
}
