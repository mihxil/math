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
package org.meeuw.math;

import java.lang.annotation.*;

/**
 * Marker for operator methods, to indicate that they are supported but non algebraically. I.e. they may not always be
 * allowed (e.g. you cannot add any two physical numbers) or they result an object not from the same algebra (e.g. the absolute value from the algebra of 'negative numbers' can be taken, but is not a negative number.
 * <p>
 * If an operation is only not possible for certain special values (like {@link org.meeuw.math.abstractalgebra.AdditiveMonoid#zero zero}), this would not make it entirely {@code NonAlgebraic}, and the {@link #reason() reason} could be indicated as {@link Reason#SOME}.
 *
 * @since 0.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD})
public @interface NonAlgebraic {

    /**
     * A description for the reason of the non-algebraicity
     * @see #reason()
     */
    String value() default "#default";


    /**
     * There are several classes of reasons for non-algebraicity.
     * <p>
     * Sometimes an operation is entirely impossible, sometimes
     * {@link Reason#TYPE it is some way possible but not inside the algebra} (e.g. the absolute value inside the algebra of negative integers).
     * <p>
     * Sometimes an operation is {@link Reason#ELEMENTS not possible for many of the elements} (e.g. the square root of real numbers is only possible for positive value)
     * <p>
     *  And sometimes the operation is only not possible {@link Reason#SOME for one or a few special elements} (e.g. the reciprocal {@link org.meeuw.math.abstractalgebra.AdditiveMonoid#zero zero}).
     */
    Reason reason() default Reason.TYPE;

    /**
     * @see NonAlgebraic#reason()
     */
    enum Reason {
        /**
         * Not algebraic because the (return) type is not in the same algebra.
         */
        TYPE,

        /**
         * Not algebraic because the operations is not possible for many elements.
         */
        ELEMENTS,

        /**
         * Not completely algebraic because the operation is not possible for all elements.
         * <p>
         * A marked exception like {@link org.meeuw.math.exceptions.DivisionByZeroException} is expected
         */
        SOME
    }
}
