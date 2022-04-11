package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.NavigableSet;
import java.util.function.BinaryOperator;

import org.meeuw.math.CollectionUtils;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;

import static org.meeuw.math.ReflectionUtils.getBinaryOperatorMethod;

/**
 * The basic operators to compare two elements. Works on two things of the same type, returning a
 * boolean.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public enum BasicComparisonOperator implements AlgebraicComparisonOperator {

    /**
     * @see AlgebraicElement#eq
     */
    EQ(
        getBinaryOperatorMethod(AlgebraicElement.class, "eq"),
        (a, b) -> a + " ≈ " + b
    ),

    /**
     * @see AlgebraicElement#neq
     */
    NEQ(
        getBinaryOperatorMethod(AlgebraicElement.class, "neq"),
        (a, b) -> a + " ≉ " + b
    ),

    /**
     * @see Object#equals(Object)
     */
    EQUALS(
        getBinaryOperatorMethod(Object.class, "equals"),
        (a, b) -> a + " = " + b
    ),

    /**
     * @see StrictlyOrdered#lt
     */
    LT(
        getBinaryOperatorMethod(StrictlyOrdered.class, "lt"),
        (a, b) -> a + " < " + b
    ),

    /**
     * @see StrictlyOrdered#lte
     */
    LTE(
        getBinaryOperatorMethod(StrictlyOrdered.class, "lte"),
        (a, b) -> a + " ≲ " + b
    ),

    /**
     * @see StrictlyOrdered#gt
     */
    GT(
        getBinaryOperatorMethod(StrictlyOrdered.class, "gt"),
        (a, b) -> a + " > " + b
    ),

    /**
     * @see StrictlyOrdered#gte
     */
    GTE(
        getBinaryOperatorMethod(StrictlyOrdered.class, "gte"),
        (a, b) -> a + " ≳ " + b
    )
    ;

    public static final NavigableSet<AlgebraicComparisonOperator> ALL = CollectionUtils.navigableSet(
        EQ, NEQ, LT, LTE, GT, GTE
    );
    public static final NavigableSet<AlgebraicComparisonOperator> ALL_AND_EQUALS = CollectionUtils.navigableSet(
        EQ, NEQ, LT, LTE, GT, GTE, EQUALS
    );


    @Getter
    final Method method;

    @Getter
    final BinaryOperator<CharSequence> stringify;

    BasicComparisonOperator(Method method, java.util.function.BinaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> boolean test(E e1, E e2) {
        return (Boolean) getMethod().invoke(e1, e2);
    }

    @Override
    public String stringify(String element1, String element2) {
        return stringify.apply(element1, element2).toString();
    }

}
