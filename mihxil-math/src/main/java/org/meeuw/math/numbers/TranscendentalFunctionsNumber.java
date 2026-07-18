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
package org.meeuw.math.numbers;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;

/**
 * Collects in an interface goniometric and other transcendental functions. {@link org.meeuw.math.abstractalgebra.CompleteField complete field} elements
 * can just extend this. But e.g. also rational number can implement them, but {@link org.meeuw.math.NonExact non exactly}
 *
 * @param <C> The type of the results of the elementary functions, this may equal to <SELF>, and it is for {@link org.meeuw.math.abstractalgebra.CompleteFieldElement complete field elements}
 *           but for example for {@code rational numbers}, these functions can be defined naturally, but their results will not be rational themselves.
 *
 * @author Michiel Meeuwissen
 * @since 0.20
 */
public interface TranscendentalFunctionsNumber<
    E extends TranscendentalFunctionsNumber<E, C>, C extends CompleteFieldElement<C>
    > {


    /**
     * The field of the return type of the functions defined in this interface.
     */
    Field<C>  getStructureOfElementaryFunctions();


    /**
     * Returns the sine of this element
     *
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SIN
     */
    C sin();

    @NonAlgebraic("Only calculable for numbers between -1 and 1")
    C asin();

    /**
     * Returns the cosine of this element
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#COS
     */
    C cos();

    @NonAlgebraic("Only calculable for numbers between -1 and 1")
    C acos();
    /**
     * Returns the tangents of this element. This default implementation is {@code sin().dividedBy(cos())}.
     */
    default C tan() {
        return sin().dividedBy(cos());
    }

    /**
     * Returns the cotangents of this element. This default implementation is {@code tan().inverse()}
     */

    default C cot() {
        return tan().inverse();
    }

    /**
     * Returns the secant of this element. This default implementation is {@code cos().inverse()}
     */

    default C sec() {
        return cos().inverse();
    }

    /**
     * Returns the cosecant of this element. This default implementation is {@code sin().inverse()}
     */

    default C csc() {
        return sin().inverse();
    }

    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#EXP
     */
    C exp();

    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#LN
     */
    C ln() throws IllegalLogarithmException;

    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SINH
     */
    default C sinh() {
        return exp().minus(negation().exp()).dividedBy(2);
    }

    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#COSH
     */
    default C cosh() {
        return exp().plus(negation().exp()).dividedBy(2);
    }

    /**
     * Returns the square root of this element.
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SQRT
     */
    C sqrt();

    E negation();


    /**
     * Returns the {@code nth} root of this element.
     * @see org.meeuw.math.operators.BasicAlgebraicIntOperator#ROOT
     */
    C root(int n);


    /**
     * Returns the power of this element to the given exponent.
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.NON_ALL_ELEMENTS, value="Can't be taken of 0 for negative arguments")
    default C pow(C exponent) throws IllegalPowerException, OverflowException {
        try {
            return ((C) (ln().times(exponent))).exp();
        } catch (IllegalLogarithmException illegalLogarithmException) {
            throw new IllegalPowerException(illegalLogarithmException, String.format("(ln().times(%s)).exp()", exponent));
        }
    }

    /**
     * Returns the <a href="https://en.wikipedia.org/wiki/Tetration">tetration</a> of this element to the given integer exponent.
     * <p>
     * Note that this can lead to very huge numbers.
     * @see BasicAlgebraicIntOperator#TETRATION
     */
    default C tetration(int height) {
        if (height < 0) {
            throw new IllegalPowerException("Cannot tetrate with negative", BasicAlgebraicIntOperator.TETRATION.stringify(toString(), Integer.toString(height)));
        }
        if (height == 0) {
            return getStructureOfElementaryFunctions().one();
        }
        return pow(tetration(height - 1));
    }

}
