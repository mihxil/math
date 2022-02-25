package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BinaryOperator;

import org.meeuw.math.exceptions.NoSuchOperatorException;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum Operator implements AlgebraicBinaryOperator {

    OPERATION(getBinaryOperatorMethod(GroupElement.class, "operate"), "*"),

    ADDITION(getBinaryOperatorMethod(AdditiveSemiGroupElement.class, "plus"), "+"),
    SUBTRACTION(getBinaryOperatorMethod(AdditiveGroupElement.class, "minus"), "-"),

    MULTIPLICATION(getBinaryOperatorMethod(MultiplicativeSemiGroupElement.class, "times"), "⋅"),
    DIVISION(getBinaryOperatorMethod(MultiplicativeGroupElement.class, "dividedBy"), "/"),

    POWER(getBinaryOperatorMethod(CompleteFieldElement.class, "pow"), "^");

    @Getter
    final Method method;

    @Getter
    final BinaryOperator<CharSequence> stringify;

    @Getter
    final String symbol;

    Operator(Method method, String symbol) {
        this.method = method;
        this.symbol = symbol;
        this.stringify = (a, b) -> a + " " + symbol + " " + b;
    }


    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E  apply(E element1, E element2) {
        try {
            if (!method.getParameterTypes()[0].isInstance(element1)) {
                throw new NoSuchOperatorException(element1.getClass().getSimpleName() + " " + element1 + " has no operator '" + method.getName() + "'");
            }
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
