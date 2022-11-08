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

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupTheory<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoidTheory<E> {

    @Property
    default void multiplicativeGroupOperators(@ForAll(STRUCTURE) MultiplicativeGroup<?> group) {
        assertThat(group.getSupportedOperators())
            .contains(BasicAlgebraicBinaryOperator.MULTIPLICATION, BasicAlgebraicBinaryOperator.DIVISION);
    }

    @Property
    default void division(
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {
        try {
            E quotient = v1.dividedBy(v2);
            E withReciprocal = v1.times(v2.reciprocal());
            assertThat(quotient.eq(withReciprocal)).withFailMessage(() ->
                String.format("%s / %s = %s != %s . %s ^ -1  = %s", v1, v2, quotient, v1, v2, withReciprocal)
            ).isTrue();
        } catch (ReciprocalException ae) {
            getLogger().info(v1 + " / " + v2 + ": " + ae.getMessage());
            assertThat(BasicAlgebraicBinaryOperator.DIVISION.isAlgebraicFor(v1)).withFailMessage(ae.getClass().getName() + " " + ae.getMessage()).isFalse();
        }
    }

    @Override
    @Property
    @Label("powNegative1 group")
    default void powNegative1(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-1).equals(v1.reciprocal())).isTrue();
        } catch (IllegalPowerException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-1) + ": " + ae.getMessage());
        }
    }
    @Override
    @Property
    @Label("powNegative2 group")
    default void powNegative2(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            E powMinus2 = v1.pow(-2);
            E oneDividedBySqr = v1.getStructure().one().dividedBy(v1.times(v1));
            assertThat(powMinus2).usingDefaultComparator().isEqualTo(oneDividedBySqr);
        } catch (IllegalPowerException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-2) + ": " + ae.getMessage());
        }
    }
    @Override
    @Property
    @Label("powNegative3 group")
    default void powNegative3(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-3)).usingDefaultComparator().isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1).times(v1)));
        } catch (IllegalPowerException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-3) + ": " + ae.getMessage());
        }
    }

    @Property(shrinking = ShrinkingMode.OFF)
    default void reciprocal(
         @ForAll(ELEMENTS) E e
    )  {
        try {
            E reciprocal = e.reciprocal();
            assertThat(reciprocal.reciprocal().eq(e)).isTrue();
            E reciprocalTimesSelf = reciprocal.times(e);
            assertThat(reciprocalTimesSelf.eq(e.getStructure().one())).withFailMessage(reciprocal + "." + e + " =" + reciprocalTimesSelf + " != " + e.getStructure().one()).isTrue();
        } catch (ReciprocalException ae) {
            // The element may be zero
            getLogger().warn("{}: {} = zero?", ae.getMessage(), e);
        }
    }




}
