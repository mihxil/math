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
package org.meeuw.test.math.uncertainnumbers;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.ConfidenceInterval;

import static org.assertj.core.api.Assertions.assertThat;

class ConfidenceIntervalTest {

    @Test
    void ofAndToString() {
        ConfidenceInterval<Double> of = ConfidenceInterval.of(10d, 2d, 2);
        assertThat(of.toString()).isEqualTo("[6.0,14.0]");
    }

    @Test
    public void test() {
        ConfidenceInterval<Double> of = ConfidenceInterval.of(10d, 2d, 2);
        assertThat(of.test(11d)).isTrue();
        assertThat(of.test(14d)).isTrue();
        assertThat(of.test(14.0000001d)).isFalse();
        assertThat(of.test(5d)).isFalse();
        assertThat(of.test(null)).isFalse();
    }

}
