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

import java.util.Optional;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.exceptions.OverflowException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.POWER;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteFieldTheory<E extends CompleteFieldElement<E>> extends
    FieldTheory<E> {

    @Property
    default void getUnary(@ForAll(STRUCTURE) AlgebraicStructure<?> struct) {
        assertThat(struct.getSupportedUnaryOperators()).contains(SQRT, SIN, COS);
    }

    @Property
    default void getOperators(@ForAll(STRUCTURE) AlgebraicStructure<?> struct) {
        assertThat(struct.getSupportedOperators()).contains(POWER);
    }

    @Property
    default void lnAndPow(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        try {
            E expectedPow = a.ln().times(b).exp();
            E pow = a.pow(b);
            assertThat(expectedPow.eq(pow))
                .withFailMessage(POWER.stringify(a, b) + " = " + pow + " ≠ " + expectedPow
                ).isTrue();
        } catch (OverflowException overflowException) {
            getLogger().info(overflowException.getMessage());
        } catch (IllegalLogarithmException illegalLogException){
            Optional<NonAlgebraic> nonalgebraicOptional = LN.getNonAlgebraic(a);
            getLogger().warn(illegalLogException.getMessage() + " (" + nonalgebraicOptional.map(Object::toString).orElse("<not marked non-algebraic>") + ")");


            assertThat(nonalgebraicOptional)
                .withFailMessage(illegalLogException.getMessage() + ". %s non algebraic for %s %s (%s)", LN, a.getClass().getSimpleName(), a, nonalgebraicOptional.get().value()).isPresent();
        }
    }

}
