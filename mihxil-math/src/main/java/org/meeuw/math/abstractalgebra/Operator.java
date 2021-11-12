package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BinaryOperator;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum Operator implements AlgebraicBinaryOperator {

    ADDITION(getBinaryOperatorMethod(AdditiveSemiGroupElement.class, "plus"), (a, b) -> a + " + " + b),
    SUBTRACTION(getBinaryOperatorMethod(AdditiveGroupElement.class, "minus"), (a, b) -> a + " - " + b),
    MULTIPLICATION(getBinaryOperatorMethod(MultiplicativeSemiGroupElement.class, "times"), (a, b) -> a + " ⋅ " + b),
    DIVISION(getBinaryOperatorMethod(MultiplicativeGroupElement.class, "dividedBy"), (a, b) -> a + " / " + b),
    POWER(getBinaryOperatorMethod(CompleteFieldElement.class, "pow"), (a, b) -> a + " ^ " + b);


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
        try {
            E result = (E) method.invoke(element1, element2);
            if (result == null) {
                throw new IllegalStateException("" + method + "(" + element1 + ',' + element2 + ") resulted null");
            }
            return result;
        } catch (IllegalArgumentException iae){
            throw new IllegalArgumentException(method.getDeclaringClass().getName() + "." + method.getName() + "(" + element1 + ',' + element2 + "): " + iae.getMessage());
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    public <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify.apply(element1.toString(), element2.toString()).toString();
    }

    @SneakyThrows
    private static Method getBinaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }

}
