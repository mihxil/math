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
package org.meeuw.test.math;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import org.meeuw.math.CollectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilsTest {

    @Test
    public void memoize() {
        AtomicInteger value = new AtomicInteger(0);

        Supplier<Integer> i = CollectionUtils.memoize(value::get);
        assertThat(i.get()).isEqualTo(0);
        assertThat(i.get()).isEqualTo(0);
        value.set(5);
        assertThat(i.get()).isEqualTo(0);
    }
}
