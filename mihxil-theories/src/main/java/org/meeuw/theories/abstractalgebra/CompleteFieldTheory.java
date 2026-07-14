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

import java.math.MathContext;
import java.util.Optional;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.configuration.ConfigurationService.Reset;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.CompleteField;
import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.MathContextConfiguration;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.configuration.ConfigurationService.setConfiguration;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.POWER;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteFieldTheory<E extends CompleteFieldElement<E>> extends
    FieldTheory<E> {

    @Property
    default void getUnary(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThat(struct.getSupportedUnaryOperators()).contains(SQRT, SIN, COS);
    }

    @Property
    default void getOperators(@ForAll(STRUCTURE) CompleteField<E> struct) {
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
            log().info(overflowException.getMessage());
        } catch (IllegalLogarithmException illegalLogException){
            Optional<NonAlgebraic> nonalgebraicOptional = LN.getNonAlgebraic(a);
            log().info(illegalLogException.getMessage() + " (" + nonalgebraicOptional.map(NonAlgebraic::value).orElse("<not marked non-algebraic>") + ")");
            assertThat(nonalgebraicOptional)
                .withFailMessage(illegalLogException.getMessage() + ". %s non algebraic for %s %s (%s)", LN, a.getClass().getSimpleName(), a, nonalgebraicOptional.get().value()).isPresent();
        }
    }

    @Property
    default void sqrt(@ForAll(ELEMENTS) E e) {
        E sqr = e.sqr();
        E sqrSrt = sqr.sqrt();
        assertThat(sqrSrt).isEqIn(e, e.negation());
    }

    @Property
    default void sin(@ForAll(ELEMENTS) E e) {
        E sin = e.sin();
        E sinAsin = null;
        try {
            sinAsin = sin.asin();
            E sin2 = sinAsin.sin();
            assertThat(sin2).withFailMessage(
                String.format("sin(asin(sin(%s))) = sin(asin(%s)) = sin(%s) = %s !=  sin(%s) = %s",
                    e, sin, sinAsin, sin2, e, sin)
            ).isEqTo(sin);
        } catch(IllegalLogarithmException ie) {
            log().warning( "sin(asin(sin(%s))) = sin(asin(%s)) = sin(%s): %s".formatted(e, sin, sinAsin, ie.getMessage()));
        }
    }

    @Property
    default void cos(@ForAll(ELEMENTS) E e) {

        E cos = e.cos();
        E cosAcos = null;
        try {
            cosAcos = cos.acos();
            E cos2 = cosAcos.cos();
            assertThat(cos2).withFailMessage(
                String.format("cos(acos(cos(%s))) = cos(acos(%s)) = cos(%s) = %s !=  cos(%s) = %s", e, cos, cosAcos, cos2, e, cos)

            ).isEqTo(cos);
        } catch(IllegalLogarithmException ie) {
            log().warning( "cos(acos(cos(%s))) = cos(acos(%s)) = cos(%s): %s".formatted(e, cos, cosAcos, ie.getMessage()));
        }
    }

    @Property
    default void pow(@ForAll(ELEMENTS) E e, @ForAll(ELEMENTS) E  exponent) {
        try {
            E pow = e.pow(exponent);
            log().info("%s = %s".formatted(POWER.stringify(e, exponent), pow));
        } catch (IllegalPowerException illegalPowerException) {
            log().info(() -> "%s = %s".formatted(POWER.stringify(e, exponent), illegalPowerException.getMessage()));
        } catch (OverflowException illegalPowerException ) {
            log().warning("%s = %s".formatted(POWER.stringify(e, exponent), illegalPowerException.getMessage()));
        }
    }

    @Property
    default void ePowZero(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThatAlgebraically(struct.e().pow(struct.zero())).isEqTo(struct.one());
    }

    @Property
    default void sinPi(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThatAlgebraically(struct.pi().sin()).isEqTo(struct.zero());
    }

    @Property
    default void asin0(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThatAlgebraically(struct.zero().asin()).isEqTo(struct.zero());
    }

    @Property
    default void asin1(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThatAlgebraically(struct.one().asin()).isEqTo(struct.pi().dividedBy(2));
    }

    @Property
    default void asinminus1(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThatAlgebraically(struct.one().negation().asin()).isEqTo(struct.pi().dividedBy(-2));
    }

    @Property
    default void cosPi(@ForAll(STRUCTURE) CompleteField<E>  struct) {
        assertThatAlgebraically(struct.pi().cos()).isEqTo(struct.one().negation());
    }

    @Property
    default void hasPi(@ForAll(STRUCTURE) CompleteField<E>  struct) {
        assertThat(struct.getConstant("pi")).isPresent();
    }

    @Property
    default void hasE(@ForAll(STRUCTURE) CompleteField<E>  struct) {
        assertThat(struct.getConstant("e")).isPresent();
    }

    @Property
    default void goldenRatio(@ForAll(STRUCTURE)  CompleteField<E> struct) {
         try (Reset res = setConfiguration(builder ->
                 builder.configure(MathContextConfiguration.class,
                     (mathContextConfiguration) -> mathContextConfiguration.withContext(new MathContext(4))))) {
             assertThatAlgebraically(struct.𝜑().sqr()).isEqTo(struct.𝜑().plus(struct.one()));
         } finally {
             ConfigurationService.resetToDefaults();
         }
    }

}
