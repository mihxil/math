/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.time;

import java.time.*;

import org.junit.jupiter.api.Test;

import org.meeuw.math.time.TestClock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class TestClockTest {

    @Test
    void withZone() {
        TestClock clock = new TestClock(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-01-15T21:39:26Z"));
        clock = clock.withZone(ZoneId.of("UTC"));
        Instant i = clock.tick();
        assertThat(i).isEqualTo(Instant.parse("2021-01-15T21:39:26Z"));
        clock.sleep(2000);
        assertThat(clock.localDateTime()).isEqualTo(LocalDateTime.of(2021, 1, 15, 21, 39, 29));
        assertThat(clock.localDateTime()).isEqualTo(LocalDateTime.now(clock));
        assertThat(clock.getZone().toString()).isEqualTo("UTC");
    }

    @Test
    void twenty() {
        TestClock test = TestClock.twentyTwenty();
        assertThat(test.instant()).isEqualTo("2020-02-20T19:20:00Z");

        assertThat(TestClock.twentyTwo().instant()).isEqualTo("2200-02-22T21:20:00Z");
    }


}
