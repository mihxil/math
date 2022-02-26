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

    OPERATION(
        getBinaryOperatorMethod(GroupElement.class, "operate"), "*",
        getUnaryMethod(Group.class, "unity"),
        getUnaryMethod(GroupElement.class, "inverse")
    ),

    ADDITION(
        getBinaryOperatorMethod(AdditiveSemiGroupElement.class, "plus"), "+",
        getUnaryMethod(AdditiveMonoid.class, "zero"),
        getUnaryMethod(AdditiveGroupElement.class, "negation")

    ),
    SUBTRACTION(
        getBinaryOperatorMethod(AdditiveGroupElement.class, "minus"), "-",
        ADDITION.unity,
        ADDITION.inverse
    ),

    MULTIPLICATION(
        getBinaryOperatorMethod(MultiplicativeSemiGroupElement.class, "times"), "⋅",
        getUnaryMethod(MultiplicativeMonoid.class, "one"),
        getUnaryMethod(MultiplicativeGroupElement.class, "reciprocal")

    ),
    DIVISION(
        getBinaryOperatorMethod(MultiplicativeGroupElement.class, "dividedBy"), "/",
        MULTIPLICATION.unity,
        MULTIPLICATION.inverse
    ),

    POWER(
        getBinaryOperatorMethod(CompleteFieldElement.class, "pow"), "^",
        MULTIPLICATION.unity,
        null
    );

    @Getter
    final Method method;

    @Getter
    final Method unity;

    @Getter
    final Method inverse;


    @Getter
    final BinaryOperator<CharSequence> stringify;

    @Getter
    final String symbol;

    Operator(Method method, String symbol, Method unity, Method inverse) {
        this.method = method;
        this.symbol = symbol;
        this.stringify = (a, b) -> a + " " + symbol + " " + b;
        this.unity = unity;
        this.inverse = inverse;
    }


    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E element1, E element2) {
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

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E  inverse(E element) {
        try {
            if (!method.getParameterTypes()[0].isInstance(element)) {
                throw new NoSuchOperatorException("");
            }
            E result = (E) method.invoke(element);
            if (result == null) {
                throw new IllegalStateException("" + method + "(" + element + ") resulted null");
            }
            return result;
        } catch (IllegalArgumentException iae){
            throw new IllegalArgumentException(method.getDeclaringClass().getName() + "." + method.getName() + "(" + element + "): " + iae.getMessage());
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>, S extends AlgebraicStructure<E>> E  unity(S structure) {
        try {
            return (E) unity.invoke(structure);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(unity.getDeclaringClass().getName() + "." + unity.getName() + "(" + structure + " ): " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }


    public <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify.apply(element1.toString(), element2.toString()).toString();
    }

    @SneakyThrows
    private static Method getBinaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }

    @SneakyThrows
    private static Method getUnaryMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name);
    }



}
