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
package org.meeuw.test.math.abstractalgebra;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class AlgebraicStructureTest {
    private static class E implements AlgebraicElement<E> {

        @Override
        public AlgebraicStructure<E> getStructure() {
            return new S();
        }
    }
    private static class S implements  AlgebraicStructure<E> {

        @Override
        public Cardinality getCardinality() {
            return Cardinality.ALEPH_0;
        }

        @Override
        public Class<E> getElementClass() {
            return E.class;
        }
    }

    private final S s = new S();

    @Test
    public void test() {
        assertThat(s.getSupportedOperators()).isEmpty();
        assertThat(s.getSupportedUnaryOperators()).containsExactly(BasicAlgebraicUnaryOperator.IDENTIFY);
        assertThat(s.getDescription()).isEqualTo("S");
        assertThat(s.getEquivalence().test(new E(), new E())).isFalse();
    }

}
