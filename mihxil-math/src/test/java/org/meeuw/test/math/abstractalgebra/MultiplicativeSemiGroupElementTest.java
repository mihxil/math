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
package org.meeuw.test.math.abstractalgebra;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class MultiplicativeSemiGroupElementTest {

    static class A implements MultiplicativeSemiGroupElement<A> {

        final int value;

        public A(int value) {
            this.value = value;
        }

        @Override
        public MultiplicativeSemiGroup<A> getStructure() {
            return new Struct();
        }

        @Override
        public A times(A multiplier) {
            return new A(value * multiplier.value);
        }
    }
    static class Struct implements MultiplicativeSemiGroup<A> {

        @Override
        public Cardinality getCardinality() {
            return Cardinality.ALEPH_0;
        }

        @Override
        public Class<A> getElementClass() {
            return A.class;
        }

        @Override
        public A nextRandom(Random random) {
            return new A(random.nextInt(100));
        }
    }

    @Test
    void pow() {
        A a = new A(2);
        assertThatThrownBy(() -> a.pow(-1)).isInstanceOf(IllegalPowerException.class);
        assertThatThrownBy(() -> a.pow(0)).isInstanceOf(IllegalPowerException.class);
        assertThat(a.pow(1).value).isEqualTo(2);
        assertThat(a.pow(2).value).isEqualTo(4);
        assertThat(a.pow(3).value).isEqualTo(8);
        assertThat(a.pow(4).value).isEqualTo(16);
        assertThat(a.pow(5).value).isEqualTo(32);
    }

}
