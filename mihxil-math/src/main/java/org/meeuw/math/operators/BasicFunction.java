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

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;

import org.meeuw.math.numbers.Sizeable;
import org.meeuw.math.numbers.SizeableScalar;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethodHandle;

@Log
public enum BasicFunction implements GenericFunction {

    /**
     * @see Sizeable#abs()
     */
    ABS(getDeclaredMethodHandle(Sizeable.class, "abs"), (s) -> "|" + s + "|"),

    /**
     * Returns the 'decimal' (actually {@link java.math.BigDecimal}) presentation of the given object.
     * @see SizeableScalar#bigDecimalValue()
     */
    DECIMAL(getDeclaredMethodHandle(SizeableScalar.class, "bigDecimalValue"), (s) ->  s + TextUtils.subscript("=")),

    /**
     * Returns an 'integer' (actually {@link java.math.BigInteger}) version of the given object.
     * This may involve rounding.
     * @see SizeableScalar#bigIntegerValue()
     */
    INTEGER(getDeclaredMethodHandle(SizeableScalar.class, "bigIntegerValue"), (s) -> "⌊" + s + "⌉");

    @Getter
    final MethodHandle method;

    final java.util.function.UnaryOperator<CharSequence> stringify;

    BasicFunction(MethodHandle method, java.util.function.UnaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public <T, R> R apply(T t) {
        try {
            try {
                return (R) method.invokeExact(t);
            } catch (IllegalArgumentException iae) {
                //log.fine(this + " on " + t + " but " + t.getClass() + " not a " + method.getDeclaringClass());
                //return (R) t.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(t);
                throw new IllegalArgumentException(iae.getMessage());
            }
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @Override
    public String stringify(String element) {
        return stringify.apply(element).toString();
    }
}
