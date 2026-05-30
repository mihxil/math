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
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;

/**
 * Collects in an interface goniometric and other transcendental functions. {@link org.meeuw.math.abstractalgebra.CompleteField complete field} elements
 * can just extend this. But e.g. also rational number can implement them, but {@link org.meeuw.math.NonExact non exactly}
 *
 * @param <E> The type of the results of the elementary functions, this may equal to <SELF>, and it is for {@link org.meeuw.math.abstractalgebra.CompleteFieldElement complete field elements}
 *           but for example for {@code rational numbers}, these functions can be defined naturally, but their results will not be rational themselves.
 *
 * @author Michiel Meeuwissen
 * @since 0.20
 */
public interface ElementaryNumber<E extends FieldElement<E>, F extends Field<E>,  SELF extends ElementaryNumber<E, F, SELF>> {


    /**
     * The field of the return type of the functions defined in this interface.
     */
    F  getStructureOfElementaryFunctions();


    /**
     * Returns the sine of this element
     *
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SIN
     */
    E sin();

    @NonAlgebraic("Only calculable for numbers between -1 and 1")
    E asin();

    /**
     * Returns the cosine of this element
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#COS
     */
    E cos();

    /**
     * Returns the tangents of this element. This default implementation is {@code sin().dividedBy(cos())}.
     */
    default E tan() {
        return sin().dividedBy(cos());
    }

    /**
     * Returns the cotangents of this element. This default implementation is {@code tan().inverse()}
     */

    default E cot() {
        return tan().inverse();
    }

    /**
     * Returns the secant of this element. This default implementation is {@code cos().inverse()}
     */

    default E sec() {
        return cos().inverse();
    }

    /**
     * Returns the cosecant of this element. This default implementation is {@code sin().inverse()}
     */

    default E csc() {
        return sin().inverse();
    }

    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#EXP
     */
    E exp();


    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#LN
     */
    E ln() throws IllegalLogarithmException;


    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SINH
     */
    default E sinh() {
        return exp().minus(negation().exp()).dividedBy(2);
    }
    /**
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#COSH
     */
    default E cosh() {
        return exp().plus(negation().exp()).dividedBy(2);
    }

    /**
     * Returns the square root of this element.
     * @see org.meeuw.math.operators.BasicAlgebraicUnaryOperator#SQRT
     */
    E sqrt();

    SELF negation();


    /**
     * Returns the {@code nth} root of this element.
     * @see org.meeuw.math.operators.BasicAlgebraicIntOperator#ROOT
     */
    E root(int n);


    /**
     * Returns the power of this element to the given exponent.
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Can't be taken of 0 for negative arguments")
    default E pow(E exponent) throws IllegalPowerException, OverflowException {
        try {
            return ((SELF) (ln().times(exponent))).exp();
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
    default E tetration(int height) {
        if (height < 0) {
            throw new IllegalPowerException("Cannot tetrate with negative", BasicAlgebraicIntOperator.TETRATION.stringify(toString(), Integer.toString(height)));
        }
        if (height == 0) {
            return getStructureOfElementaryFunctions().one();
        }
        return pow(tetration(height -1));
    }

}
