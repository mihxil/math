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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.Magma;
import org.meeuw.math.abstractalgebra.MagmaElement;
import org.opentest4j.TestAbortedException;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.OPERATION;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface MagmaTheory<E extends MagmaElement<E>>
    extends AlgebraicStructureTheory<E> {

    @Property
    default void magmaOperators(@ForAll(STRUCTURE) Magma<E> s) {
        assertThat(s.getSupportedOperators()).contains(OPERATION);
    }

    @Property
    default void operatorAndCommutativity(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENT) E e2) {
        boolean isCommutative = e1.getStructure().operationIsCommutative();
        if (isCommutative) {
            assertThat(e1.operate(e2)).withFailMessage(
                OPERATION.stringify(e1, e2)  + " should be " +
                    OPERATION.stringify(e2, e1)

            ).isEqTo(e2.operate(e1));
        } else {
            try {
                assertThat(e1.operate(e2)).withFailMessage(
                    OPERATION.stringify(e1, e2) + " should not be " +
                        OPERATION.stringify(e2, e1)

                ).isNotEqTo(e2.operate(e1));
            } catch (AssertionError ae) {
                throw new TestAbortedException(ae.getMessage());
            }
        }
    }}
