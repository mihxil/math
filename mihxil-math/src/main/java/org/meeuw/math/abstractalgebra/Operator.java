package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum Operator {
    ADDITION(getBinaryOperator(AdditiveMonoidElement.class, "plus"), "+"),
    SUBTRACTION(getBinaryOperator(AdditiveGroupElement.class, "minus"), "-"),
    MULTIPLICATION(getBinaryOperator(MultiplicativeSemiGroupElement.class, "times"), "⋅"),
    DIVISION(getBinaryOperator(MultiplicativeGroupElement.class, "dividedBy"), "/");

    @Getter
    final Method method;

    @Getter
    final String symbol;

    Operator(Method method, String symbol) {
        this.method = method;
        this.symbol = symbol;
    }

    @SneakyThrows
    public static Method getBinaryOperator(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
