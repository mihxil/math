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

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.math.text.TextUtils;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.14
 */
public interface AlgebraicIntOperator extends OperatorInterface {

    <E extends AlgebraicElement<E>> E apply(E e, int i);

    String stringify(String element, String i);

    default <E extends AlgebraicElement<E>> String stringify(E element1, int i) {
        return stringify(element1.toString(), "" + i);
    }

    default String getSymbol() {
        return stringify(TextUtils.PLACEHOLDER, "n");
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows
    static  <E extends AlgebraicElement<E>> E apply(AlgebraicIntOperator operator, Method method, E e, int i) {
        try {
            return (E) method.invoke(e, i);
        } catch (IllegalArgumentException iae) {
            try {
                // It is possible that the operation is defined, but the class does not extend the correct class
                // e.g. an odd integer implements negation, but it is not an additive group (negation is possible inside the algebra, but addition itself isn't).
                return (E) e.getClass().getMethod(method.getName(), int.class).invoke(e, i);
            } catch (NoSuchMethodException noSuchMethodError) {
                throw new NoSuchOperatorException("No operation " + operator + " found on " + e, noSuchMethodError);
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

}
