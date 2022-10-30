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
package org.meeuw.test.math.numbers;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;

public class ScalarTest {

    @Test
    public void number() {
        Scalar<?> d = new Scalar.Number(10.234);
        assertThat(d.signum()).isEqualTo(1);
        assertThat(d.doubleValue()).isEqualTo(10.234d);
        assertThat(d.longValue()).isEqualTo(10);
        assertThat(d.floatValue()).isEqualTo(10.234f);
        assertThat(d.intValue()).isEqualTo(10);
        assertThat(d.bigIntegerValue()).isEqualTo(BigInteger.valueOf(10));
        assertThat(d.abs().intValue()).isEqualTo(10);
        assertThat(d.bigDecimalValue()).isEqualTo("10.234");

        assertThat(new Scalar.Number(0L).signum()).isEqualTo(0);

        assertThat(new Scalar.Number(0L).compareTo(new Scalar.Number(2L))).isEqualTo(-1);
    }
}
