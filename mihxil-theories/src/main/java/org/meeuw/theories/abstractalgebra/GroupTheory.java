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
package org.meeuw.theories.abstractalgebra;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.AlgebraicElement.eqComparator;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface GroupTheory<E extends GroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void groupOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.OPERATION);
    }

    @Property
    default void groupUnitaryOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedUnaryOperators()).contains(BasicAlgebraicUnaryOperator.INVERSION);
    }

    @Property
    default void operateAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.operate(v2)).operate(v3))
            .usingComparator(eqComparator())
            .isEqualTo(v1.operate((v2.operate(v3))));
    }

    @Property
    default void unity(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.operate(v.getStructure().unity()).eq(v))
            .withFailMessage(String.format("Unity(%s) = %s", v, v.self()))
            .isTrue();
    }

    @Property
    default void inverse(
        @ForAll(ELEMENTS) E v) {
        withLooseEquals(() -> {
            try {
                E inverse = v.inverse().operate(v);
                assertThat(inverse.eq(v.getStructure().unity()))
                    .withFailMessage(() -> "inverse " + v.inverse() + " · " + v + " != " + v.getStructure().unity() + " (but " + inverse + ")").isTrue();
            } catch (InverseException | IllegalPowerException ie) {
                Assume.that(!BasicAlgebraicUnaryOperator.INVERSION.isAlgebraicFor(v));

                log().info(ie.getMessage());
            }
        });
    }

    class UnknownGroupElement implements GroupElement<UnknownGroupElement> {

        @Override
        public UnknownGroup getStructure() {
            return null;
        }

        @Override
        public UnknownGroupElement operate(UnknownGroupElement operand) {
            return null;
        }

        @Override
        public UnknownGroupElement inverse() {
            return null;
        }
    }
    class UnknownGroup implements Group<UnknownGroupElement> {

        @Override
        public Cardinality getCardinality() {
            return null;
        }

        @Override
        public Class<UnknownGroupElement> getElementClass() {
            return UnknownGroupElement.class;
        }

        @Override
        public UnknownGroupElement unity() {
            return null;
        }
    }

    @Property
    default void castingError(@ForAll(ELEMENTS) E v) {
        assertThatThrownBy(() -> {
            v.cast(UnknownGroupElement.class);
        }).isInstanceOf(NotASubGroup.class);
    }

}
