package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.function.BinaryOperator;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum Operator implements AlgebraicBinaryOperator {
    ADDITION(getBinaryOperator(AdditiveSemiGroupElement.class, "plus"), (a, b) -> a + "+" + b),
    SUBTRACTION(getBinaryOperator(AdditiveGroupElement.class, "minus"), (a, b) -> a + "-" + b),
    MULTIPLICATION(getBinaryOperator(MultiplicativeSemiGroupElement.class, "times"), (a, b) -> a + "⋅" + b),
    DIVISION(getBinaryOperator(MultiplicativeGroupElement.class, "dividedBy"), (a, b) -> a + "/" + b);

    @Getter
    final Method method;

    @Getter
    final BinaryOperator<CharSequence> stringify;

    Operator(Method method, BinaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public <E extends AlgebraicElement<E>> E  apply(E element1, E element2) {
        return (E) getMethod().invoke(element1, element2);
    }

    public  <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify.apply(element1.toString(), element2.toString()).toString();

    }

    @SneakyThrows
    public static Method getBinaryOperator(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }

}
