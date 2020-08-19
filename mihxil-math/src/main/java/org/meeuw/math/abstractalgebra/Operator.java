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
public enum Operator implements AlgebraicBinaryOperator {
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


    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public <E extends AlgebraicElement<E>> E  apply(E element1, E element2) {
        return (E) getMethod().invoke(element1, element2);
    }

    @SneakyThrows
    public static Method getBinaryOperator(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }

}
