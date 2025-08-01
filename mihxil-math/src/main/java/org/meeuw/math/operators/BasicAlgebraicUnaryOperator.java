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

import java.lang.invoke.MethodHandle;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethodHandle;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NoSuchOperatorException;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * The predefined  basic 'unary operators' of algebra's.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public enum BasicAlgebraicUnaryOperator implements AlgebraicUnaryOperator {

    /**
     * @see AlgebraicElement#self()
     */
    IDENTIFY(
        getDeclaredMethodHandle(AlgebraicElement.class, "self"),
        (s) -> !s.isEmpty() && s.charAt(0) == '+' ? s : "+" + s
    ),

    /**
     * @see AdditiveGroupElement#negation()
     */
    NEGATION(
        getDeclaredMethodHandle(AdditiveGroupElement.class, "negation"),
        (s) -> !s.isEmpty() && s.charAt(0) == '-' ? "+" + s.subSequence(1, s.length()) : "-" + s),

    /**
     * @see MultiplicativeGroupElement#reciprocal()
     */
    RECIPROCAL(
        getDeclaredMethodHandle(MultiplicativeGroupElement.class, "reciprocal"),
        (s) -> s + superscript(-1)
    ),

    /**
     * @see GroupElement#inverse()
     */
    INVERSION(
        getDeclaredMethodHandle(GroupElement.class, "inverse"),
        (s) -> "inverse(" + s  + ")"
    ),

    /**
     * @see MultiplicativeSemiGroupElement#sqr()
     */
    SQR(
        getDeclaredMethodHandle(MultiplicativeSemiGroupElement.class, "sqr"),
        (s) -> (s.toString().contains(" ") ? "(" + s  + ")" : s ) + superscript(2)
    ),


    /**
     * @see CompleteFieldElement#sqrt()
     */
    SQRT(
        getDeclaredMethodHandle(CompleteFieldElement.class, "sqrt"),
        (s) -> "√" + (s.length() > 1 ?"(" + s + ")" : s)
    ),

    /**
     * @see CompleteFieldElement#sin()
     */
    SIN(
        getDeclaredMethodHandle(CompleteFieldElement.class, "sin"),
        (s) -> "sin(" + s + ")"
    ),

    /**
     * @see CompleteFieldElement#cos()
     */
    COS(
        getDeclaredMethodHandle(CompleteFieldElement.class, "cos"),
        (s) -> "cos(" + s + ")"
    ),

    /**
     * @see CompleteFieldElement#exp()
     */
    EXP(
        getDeclaredMethodHandle(CompleteFieldElement.class, "exp"),
        (s) -> "exp(" + s + ")"
    ),

    /**
     * @see CompleteFieldElement#ln()
     */
    LN(
        getDeclaredMethodHandle(CompleteFieldElement.class, "ln"),
        (s) -> "ln(" + s + ")"
    ),

    /**
     * @see CompleteFieldElement#sinh()
     */
    SINH(
        getDeclaredMethodHandle(CompleteFieldElement.class, "sinh"),
        (s) -> "sinh(" + s + ")"
    ),

     /**
     * @see CompleteFieldElement#cosh()
     */
    COSH(
        getDeclaredMethodHandle(CompleteFieldElement.class, "cosh"),
        (s) -> "cosh(" + s + ")"
    )

    ;

    @Getter
    final MethodHandle method;

    final java.util.function.UnaryOperator<CharSequence> stringify;

    BasicAlgebraicUnaryOperator(MethodHandle method, java.util.function.UnaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e) {
        try {
            return (E) method.invoke(e);
        } catch (IllegalArgumentException iae) {

                // It is possible that the operation is defined, but the class does not extend the correct class
                // e.g. an odd integer implements negation, but it is not an additive group (negation is possible inside the algebra, but addition itself isn't).
                //return (E) e.getClass().getMethod(method.getName()).invoke(e);
                throw iae;

        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @Override
    public String stringify(String element1) {
        return stringify.apply(element1).toString();
    }

}
