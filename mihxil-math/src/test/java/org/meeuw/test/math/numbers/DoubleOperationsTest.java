/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.numbers;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.assertj.core.data.Offset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.numbers.DoubleOperations.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class DoubleOperationsTest {

    @Test
    void getFractionalUncertainty() {
        assertThat(INSTANCE.getFractionalUncertainty(1d, 0.5))
            .isEqualTo(0.3333333333333333);
    }

    @Test
    void  sqr() {
        assertThat(INSTANCE.sqr(5.d)).isEqualTo(25d);
    }

    @Test
    void sqrt() {
        assertThat(INSTANCE.sqrt(25.d).getValue()).isEqualTo(5d);
    }

    @Test
    void  abs() {
        assertThat(INSTANCE.abs(25.d)).isEqualTo(25d);
        assertThat(INSTANCE.abs(-25.d)).isEqualTo(25d);
        assertThat(INSTANCE.abs(0.d)).isEqualTo(0d);
    }

    @Test
    void reciprocal() {
        assertThat(INSTANCE.reciprocal(5d).getValue()).isEqualTo(0.2);
        assertThat(INSTANCE.reciprocal(-5d).getValue()).isEqualTo(-0.2);
        assertThatThrownBy(() -> INSTANCE.reciprocal(0d)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void negate() {
        assertThat(INSTANCE.negate(5d)).isEqualTo(-5d);
    }

    @Test
    void multiply() {
        assertThat(INSTANCE.multiply(5d, 3d)).isEqualTo(15d);
        assertThat(INSTANCE.multiply(5d, 3d, -4d)).isEqualTo(-60);
        assertThat(INSTANCE.multiply(5, 3d)).isEqualTo(15);
    }

    @Test
    void divide() {
        assertThat(INSTANCE.divide(1d, 2d).getValue()).isEqualTo(0.5);
    }

    @Test
    void add() {
        assertThat(INSTANCE.add(1d, 2d)).isEqualTo(3.0);
    }

    @Test
    void testAdd() {
        assertThat(INSTANCE.add(1d, 2d, 3d)).isEqualTo(6d);
    }

    @Test
    void pow() {
        assertThat(INSTANCE.pow(2d, 3d).getValue()).isEqualTo(8d);
    }

    @Test
    void testPow() {
        assertThat(INSTANCE.pow(2d, 3)).isEqualTo(8d);
    }

    @Test
    void lt() {
        assertThat(INSTANCE.lt(1d, 2d)).isTrue();
        assertThat(INSTANCE.lt(2d, 2d)).isFalse();
    }

    @Test
    void lte() {
        assertThat(INSTANCE.lte(1d, 2d)).isTrue();
        assertThat(INSTANCE.lte(2d, 2d)).isTrue();
    }

    @Test
    void gte() {
        assertThat(INSTANCE.gte(1d, 2d)).isFalse();
        assertThat(INSTANCE.gte(2d, 1d)).isTrue();
        assertThat(INSTANCE.gte(2d, 2d)).isTrue();
    }

    @Test
    void isFinite() {
        assertThat(INSTANCE.isFinite(1d)).isTrue();
        assertThat(INSTANCE.isFinite(Double.NEGATIVE_INFINITY)).isFalse();
    }

    @Test
    void isNaN() {
        assertThat(INSTANCE.isNaN(1d)).isFalse();
        assertThat(INSTANCE.isNaN(Double.NEGATIVE_INFINITY)).isFalse();
        assertThat(INSTANCE.isNaN(Double.NaN)).isTrue();
    }

    @Test
    void signum() {
        assertThat(INSTANCE.signum(1d)).isEqualTo(1);
        assertThat(INSTANCE.signum(-10d)).isEqualTo(-1);
        assertThat(INSTANCE.signum(0d)).isEqualTo(0);
    }

    @Test
    void bigDecimalValue() {
        assertThat(INSTANCE.bigDecimalValue(123.456d)).isEqualTo(new BigDecimal("123.456"));
    }

    @Test
    void sin() {
        assertThat(INSTANCE.sin(Math.PI).getValue()).isCloseTo(0, Offset.offset(0.0001));
    }

    @Test
    void cos() {
        assertThat(INSTANCE.cos(Math.PI).getValue()).isCloseTo(-1, Offset.offset(0.0001));
    }

    @Test
    void max() {
        assertThat(INSTANCE.max(1d, 2d, 3d, 3d, 4d, 5d, -10d, -2d)).isEqualTo(5d);
    }
    @Test
    void min() {
        assertThat(INSTANCE.min(1d, 2d, 3d, 3d, 4d, 5d, -10d, -2d)).isEqualTo(-10d);
    }
}
