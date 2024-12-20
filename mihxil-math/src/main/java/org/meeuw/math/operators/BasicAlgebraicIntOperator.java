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
import lombok.extern.java.Log;

import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;

/**
 * The predefined  basic 'operators' of algebra's that just work on an int.
 * @author Michiel Meeuwissen
 * @since 0.14
 */
@Log
public enum BasicAlgebraicIntOperator implements AlgebraicIntOperator {

    POWER(
        getDeclaredMethod(MultiplicativeSemiGroupElement.class, "pow", int.class),
        (s, i) ->  withBracketsIfNeeded(s) + TextUtils.superscript(i)
    ),
    ROOT(
        getDeclaredMethod(CompleteFieldElement.class, "root", int.class),
        (s, i) ->  TextUtils.superscript(i) + "√" + withBracketsIfNeeded(s)
    ),
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
