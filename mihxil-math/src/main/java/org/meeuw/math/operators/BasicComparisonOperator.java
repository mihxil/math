package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.NavigableSet;
import java.util.function.BinaryOperator;

import org.meeuw.math.CollectionUtils;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;

/**
 * The basic operators to compare two elements. Works on two things of the same type, returning a
 * boolean.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public enum BasicComparisonOperator implements AlgebraicComparisonOperator {

    EQ(getBinaryOperatorMethod(AlgebraicElement.class, "eq"), (a, b) -> a + "≈" + b),
    EQUALS(getBinaryOperatorMethod(Object.class, "equals"), (a, b) -> a + "=" + b),
    LT(getBinaryOperatorMethod(StrictlyOrdered.class, "lt"), (a, b) -> a + "<" + b),
    LTE(getBinaryOperatorMethod(StrictlyOrdered.class, "lte"), (a, b) -> a + "<=" + b),
    GT(getBinaryOperatorMethod(StrictlyOrdered.class, "gt"), (a, b) -> a + ">" + b),
    GTE(getBinaryOperatorMethod(StrictlyOrdered.class, "gte"), (a, b) -> a + ">=" + b),
    ;

    public static final NavigableSet<AlgebraicComparisonOperator> ALL = CollectionUtils.navigableSet(
        EQ, LT, LTE, GT, GTE
    );
    public static final NavigableSet<AlgebraicComparisonOperator> ALL_AND_EQUALS = CollectionUtils.navigableSet(
        EQ, LT, LTE, GT, GTE, EQUALS
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

    @SneakyThrows
    public static Method getBinaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }
}
