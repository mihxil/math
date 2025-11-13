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
package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;

/**
 * The predefined basic 'operators' of algebra's that just work on an int.
 * @author Michiel Meeuwissen
 * @since 0.14
 */
public enum BasicAlgebraicIntOperator implements AlgebraicIntOperator {

    /**
     * Raising to an integer power, which is defined for all {@link MultiplicativeSemiGroupElement}s.
     */
    POWER(
        getDeclaredMethod(MultiplicativeSemiGroupElement.class, "pow", int.class),
        (s, i) ->  withBracketsIfNeeded(s) + TextUtils.superscript(i)
    ),

    /**
     * Scale by power of 10, which is defined for all {@link MultiplicativeSemiGroupElement}s.
     * @since 0.19
     */
    SCALE_BY_POWER_OF_10(
        getDeclaredMethod(DivisibleGroupElement.class, "scaleByPowerOfTen", int.class),
        (s, i) ->  withBracketsIfNeeded(s) + (TextUtils.TIMES + "10") + TextUtils.superscript(i)
    ),

    /**
     * Scale by  power of 2, which is defined for all {@link MultiplicativeSemiGroupElement}s.
     * @since 0.19
     */
    SCALB(
        getDeclaredMethod(DivisibleGroupElement.class, "scalb", int.class),
        (s, i) ->  withBracketsIfNeeded(s) + (TextUtils.TIMES + "2") + TextUtils.superscript(i)
    ),

    /**
     * Taking the n-th root of an element, which is defined for all {@link CompleteFieldElement}s.
     */
    ROOT(
        getDeclaredMethod(CompleteFieldElement.class, "root", int.class),
        (s, i) ->  TextUtils.superscript(i) + "√" + withBracketsIfNeeded(s)
    ),

    /**
     * Taking the n-th tetration of an element.
     */
    TETRATION(
        getDeclaredMethod(CompleteFieldElement.class, "tetration", int.class),
        (s, i) -> TextUtils.superscript(i) + withBracketsIfNeeded(s)
    )
    ;

    private static CharSequence withBracketsIfNeeded(CharSequence s) {
        return needsBrackets(s) ? ("(" + s + ")") : s;
    }

    private static boolean needsBrackets(CharSequence s) {
        return s.toString().contains(" ");
    }



    @Getter
    final Method method;

    final java.util.function.BiFunction<CharSequence, CharSequence, String> stringify;

    BasicAlgebraicIntOperator(Method method, java.util.function.BiFunction<CharSequence, CharSequence, String> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e, int i) {
        return AlgebraicIntOperator.apply(this, method, e, i);
    }

    @Override
    public String stringify(String element, String n) {
        return stringify.apply(element, n);
    }

}
