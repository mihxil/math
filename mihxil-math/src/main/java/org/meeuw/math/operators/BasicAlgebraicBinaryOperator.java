package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BinaryOperator;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.exceptions.NoSuchOperatorException;

import static org.meeuw.math.ReflectionUtils.getBinaryOperatorMethod;
import static org.meeuw.math.ReflectionUtils.getUnaryOperatorMethod;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum BasicAlgebraicBinaryOperator implements AlgebraicBinaryOperator {

    /**
     * @see MagmaElement#operate(MagmaElement)
     */
    OPERATION(
        getBinaryOperatorMethod(MagmaElement.class, "operate"), "*",
        getUnaryOperatorMethod(Group.class, "unity"),
        BasicAlgebraicUnaryOperator.INVERSION
    ),

    /**
     * @see AdditiveSemiGroupElement#plus(AdditiveSemiGroupElement)
     */
    ADDITION(
        getBinaryOperatorMethod(AdditiveSemiGroupElement.class, "plus"), "+",
        getUnaryOperatorMethod(AdditiveMonoid.class, "zero"),
        BasicAlgebraicUnaryOperator.NEGATION
    ),

    /**
     * @see AdditiveGroupElement#minus(AdditiveGroupElement)
     */
    SUBTRACTION(
        getBinaryOperatorMethod(AdditiveGroupElement.class, "minus"), "-",
        ADDITION.unity,
        ADDITION.inverse
    ),


    /**
     * @see MultiplicativeSemiGroupElement#times(MultiplicativeSemiGroupElement)
     */
    MULTIPLICATION(
        getBinaryOperatorMethod(MultiplicativeSemiGroupElement.class, "times"), "⋅",
        getUnaryOperatorMethod(MultiplicativeMonoid.class, "one"),
        BasicAlgebraicUnaryOperator.RECIPROCAL
    ),

    /**
     * @see MultiplicativeGroupElement#dividedBy(MultiplicativeGroupElement)
     */
    DIVISION(
        getBinaryOperatorMethod(MultiplicativeGroupElement.class, "dividedBy"), "/",
        MULTIPLICATION.unity,
        MULTIPLICATION.inverse
    ),

    /**
     * @see CompleteFieldElement#pow(CompleteFieldElement)
     */
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
    @Nullable
    final BasicAlgebraicUnaryOperator inverse;


    @Getter
    final BinaryOperator<CharSequence> stringify;

    @Getter
    final String symbol;

    BasicAlgebraicBinaryOperator(Method method, String symbol, Method unity, BasicAlgebraicUnaryOperator inverse) {
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
                throw new InvalidAlgebraicResult("" + method + "(" + element1 + ',' + element2 + ") resulted null");
            }
            return result;
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    /**
     * Obtains the inverse element associated with this operator.
     *
     * E.g. if this operator is {@link #ADDITION} or {@link #SUBTRACTION} it will return the associated the negative of the argument.
     *
     * @param element The element to obtain an inverse for.
     */
    public <E extends AlgebraicElement<E>> E  inverse(E element) {
        if (inverse == null) {
            throw new NoSuchOperatorException("No inverse function for operation " + this + " defined");
        }
        E result = inverse.apply(element);
        if (result == null) {
            throw new InvalidAlgebraicResult("" + inverse + "(" + element + ") resulted null");
        }
        return result;
    }

    /**
     * Obtains the unity element associated with this operator.
     *
     * E.g. if this operator is {@link #ADDITION} or {@link #SUBTRACTION} it will return the associated {@code zero} element
     *
     * @param structure The structure to obtain if for.
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>, S extends AlgebraicStructure<E>> E  unity(S structure) {
        try {
            return (E) unity.invoke(structure);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(unity.getDeclaringClass().getName() + "." + unity.getName() + "(" + structure + " ): " + e.getMessage());
        } catch (IllegalArgumentException iae) {
            throw new NoSuchOperatorException(iae);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public String stringify(String element1, String element2) {
        return stringify.apply(element1, element2).toString();
    }

    @Override
    public String getMethodName() {
        return method.getName();
    }



}
