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
package org.meeuw.math.windowed;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import org.meeuw.math.TestClock;
import org.meeuw.math.statistics.StatisticalDoubleImpl;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class WindowedStatisticalDoubleTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();

        BiConsumer<Windowed.Event, Windowed<StatisticalDoubleImpl>> listener =
            (event, statisticalDouble) -> {
                log.info("{}", statisticalDouble);
        };

        WindowedStatisticalDouble impl = WindowedStatisticalDouble
            .builder()
            .bucketDuration(Duration.ofMillis(4))
            .bucketCount(30)
            .eventListeners(listener)
            .clock(clock)
            .build();

        impl.accept(0.1, 0.2);
        clock.sleep(1);
        impl.accept(0.2, 0.21);
        clock.sleep(1);
        impl.accept(0.19);
        clock.sleep(1);
        impl.accept(0.22, 0.23);
        clock.sleep(1);
        impl.accept(0.24);
        log.info(() -> String.valueOf(impl));
        StatisticalDoubleImpl windowValue = impl.getWindowValue();
        UncertainDoubleElement uncertainNumber = windowValue.immutableCopy();
        assertThat(windowValue.toString()).isEqualTo("0.20 ± 0.04");
        assertThat(uncertainNumber.toString()).isEqualTo("0.20 ± 0.04");
        assertThat(windowValue.getCount()).isEqualTo(8);
    }



}
