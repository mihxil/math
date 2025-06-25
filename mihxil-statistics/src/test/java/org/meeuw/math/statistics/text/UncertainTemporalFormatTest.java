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
package org.meeuw.math.statistics.text;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.meeuw.time.text.UncertainTimeFormat;

/**
 * @author Michiel Meeuwissen
 */
class UncertainTemporalFormatTest {

    @Test
    void format() {
        UncertainTimeFormat format = new UncertainTimeFormat();
        assertThatThrownBy(() -> format.format(new Object())).isInstanceOf(IllegalArgumentException.class);
    }
}
