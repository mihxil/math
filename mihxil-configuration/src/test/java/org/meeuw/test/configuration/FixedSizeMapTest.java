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
package org.meeuw.test.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.FixedSizeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class FixedSizeMapTest {

    @Test
    public void test() {
        Map<String, Integer> test = new LinkedHashMap<>();
        test.put("a", 1);
        test.put("b", 1);
        FixedSizeMap<String, Integer> fixed = new FixedSizeMap<>(test);

        fixed.put("a", 2);
        assertThatThrownBy(() -> fixed.put("c", 3)).isInstanceOf(UnsupportedOperationException.class);

        assertThat(fixed.size()).isEqualTo(2);
        assertThat(fixed.get("a")).isEqualTo(2);

        assertThat(fixed.toString()).isEqualTo("{a=2, b=1}");

        assertThatThrownBy(() -> fixed.remove("a")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testWithKeys() {

        FixedSizeMap<String, Integer> fixed = new FixedSizeMap<>("a", "b");
        fixed.put("a", 1);
        fixed.put("b", 1);

        assertThatThrownBy(() -> fixed.put("c", 3)).isInstanceOf(UnsupportedOperationException.class);

        fixed.put("a", 2);
        assertThat(fixed.size()).isEqualTo(2);
        assertThat(fixed.get("a")).isEqualTo(2);

        assertThat(fixed.toString()).isEqualTo("{a=2, b=1}");
    }



    @Test
    public void testOf() {

        FixedSizeMap<String, Integer> fixed = FixedSizeMap.of("a", 1,  "b", 1);
        fixed.put("a", 3);

        assertThatThrownBy(() -> fixed.put("c", 3)).isInstanceOf(UnsupportedOperationException.class);

        fixed.put("a", 2);
        assertThat(fixed.size()).isEqualTo(2);
        assertThat(fixed.get("a")).isEqualTo(2);

        assertThat(fixed.toString()).isEqualTo("{a=2, b=1}");

        assertThatThrownBy(() -> FixedSizeMap.of("a", 1, "b", 2, "c")).isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> FixedSizeMap.of("a", 1, 2, "b")).isInstanceOf(ClassCastException.class);
        assertThatThrownBy(() -> FixedSizeMap.of("a", 1, "b", "b")).isInstanceOf(ClassCastException.class);
        assertThatThrownBy(() -> FixedSizeMap.of("a", null, "b", 2, "c", "c")).isInstanceOf(ClassCastException.class);

        assertThatThrownBy(() -> FixedSizeMap.of("a", 1, null, 2)).isInstanceOf(IllegalArgumentException.class);
    }
}
