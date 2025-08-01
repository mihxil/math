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

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.NavigableSet;
import java.util.function.BinaryOperator;

import org.meeuw.math.CollectionUtils;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredBinaryMethodHandle;

/**
 * The basic operators to compare two elements. Works on two things of the same type, returning a
 * boolean.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public enum BasicComparisonOperator implements AlgebraicComparisonOperator {

    /**
     * @see AlgebraicElement#eq
     */
    EQ(
        getDeclaredBinaryMethodHandle(AlgebraicElement.class, "eq"),
        (a, b) -> a + " ≈ " + b
    ),

    /**
     * @see AlgebraicElement#neq
     */
    NEQ(
        getDeclaredBinaryMethodHandle(AlgebraicElement.class, "neq"),
        (a, b) -> a + " ≉ " + b
    ),

    /**
     * @see Object#equals(Object)
     */
    EQUALS(
        getDeclaredBinaryMethodHandle(Object.class, "equals"),
        (a, b) -> a + " = " + b
    ),

    /**
     * @see StrictlyOrdered#lt
     */
    LT(
        getDeclaredBinaryMethodHandle(StrictlyOrdered.class, "lt"),
        (a, b) -> a + " < " + b
    ),

    /**
     * @see StrictlyOrdered#lte
     */
    LTE(
        getDeclaredBinaryMethodHandle(StrictlyOrdered.class, "lte"),
        (a, b) -> a + " ≲ " + b
    ),

    /**
     * @see StrictlyOrdered#gt
     */
    GT(
        getDeclaredBinaryMethodHandle(StrictlyOrdered.class, "gt"),
        (a, b) -> a + " > " + b
    ),

    /**
     * @see StrictlyOrdered#gte
     */
    GTE(
        getDeclaredBinaryMethodHandle(StrictlyOrdered.class, "gte"),
        (a, b) -> a + " ≳ " + b
    )
    ;

    public static final NavigableSet<AlgebraicComparisonOperator> ALL = CollectionUtils.navigableSet(
        EQ, NEQ, LT, LTE, GT, GTE
    );
    public static final NavigableSet<AlgebraicComparisonOperator> ALL_AND_EQUALS = CollectionUtils.navigableSet(
        EQ, NEQ, LT, LTE, GT, GTE, EQUALS
    );


    @Getter
    final MethodHandle method;

    @Getter
    final BinaryOperator<CharSequence> stringify;

    BasicComparisonOperator(MethodHandle method, java.util.function.BinaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> boolean test(E e1, E e2) {
        return (Boolean) getMethod().invoke(e1, e2);
    }

    @Override
    public String stringify(String element1, String element2) {
        return stringify.apply(element1, element2).toString();
    }

}
