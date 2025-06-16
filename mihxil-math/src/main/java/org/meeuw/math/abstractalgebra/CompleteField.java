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
package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.Synonym;
import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.POWER;
import static org.meeuw.math.operators.BasicAlgebraicIntOperator.ROOT;
import static org.meeuw.math.operators.BasicAlgebraicIntOperator.TETRATION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.*;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Complete_field">complete field</a> element has no 'gaps', which means e.g. that operations like
 *  * {@link CompleteFieldElement#sqrt()} and trigonometric operations like {@link CompleteFieldElement#sin()} are possible.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteField<E extends CompleteFieldElement<E>> extends Field<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(ScalarField.OPERATORS, POWER);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(ScalarField.UNARY_OPERATORS, SQRT, SIN, COS, EXP, LN, SINH, COSH);

    NavigableSet<AlgebraicIntOperator> INT_OPERATORS = navigableSet(MultiplicativeSemiGroup.INT_OPERATORS, ROOT, TETRATION);


    /**
     * 𝜋, the ratio of the circumference of a circle to its diameter, approximately 3.14159.
     * @see #𝜋()
     */
    E pi();

    /**
     * Euler's number, the base of the natural logarithm, approximately 2.71828.
     */
    E e();


    /**
     * @see #pi()
     */
    @Synonym("pi")
    default E 𝜋() {
        return pi();
    }

    /**
     * Golden ratio (1 + √5) / 2, approximately 1.61803.
     */
    E 𝜑();

    default E element(long n) {
        return one().times(n);
    }

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicIntOperator> getSupportedIntOperators() {
        return INT_OPERATORS;
    }


    @Override
    default E determinant(E[][] source) {
        // we have comparison and abs, we could use Gaussion elimination with partial pivoting
        return Field.super.determinant(source);
    }

    /**
     * Static version of {@link CompleteFieldElement#sin()}, for a more natural syntax in expressions (e.g. {@code sin(angle)}, rather than {@code angle.sin()}).
     * @see CompleteFieldElement#sin()
     */
    @Synonym("angle.sin()")
    static <E extends CompleteFieldElement<E>> E sin(@radians E angle) {
        return angle.sin();
    }

    /**
     * Static version of {@link CompleteFieldElement#cos()}, for a more natural syntax in expressions (e.g. {@code cos(angle)}, rather than {@code angle.cos()}).
     * @see CompleteFieldElement#cos()
     */
    @Synonym("angle.cos()")
    static <E extends CompleteFieldElement<E>> E cos(@radians E angle) {
        return angle.cos();
    }

    /**
     * Static version of {@link CompleteFieldElement#sqrt()}, for a more natural syntax in expressions (e.g. {@code sqrt(x)}, rather than {@code x.sqrt()}).
     * @see CompleteFieldElement#sqrt()
     */
    @Synonym("angle.sqrt()")
    static <E extends CompleteFieldElement<E>> E sqrt(E x) {
        return x.sqrt();
    }

}
