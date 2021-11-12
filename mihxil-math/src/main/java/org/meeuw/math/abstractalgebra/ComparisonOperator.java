package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public enum ComparisonOperator implements AlgebraicComparisonOperator {

    EQUALS(getBinaryOperatorMethod(AlgebraicElement.class, "eq"), (a, b) -> a + "=" + b),
    LT(getBinaryOperatorMethod(Ordered.class, "lt"), (a, b) -> a + "<" + b),
    LTE(getBinaryOperatorMethod(Ordered.class, "lte"), (a, b) -> a + "<=" + b),
    GT(getBinaryOperatorMethod(Ordered.class, "gt"), (a, b) -> a + ">" + b),
    GTE(getBinaryOperatorMethod(Ordered.class, "gte"), (a, b) -> a + ">=" + b),
    ;

    public static Set<ComparisonOperator> ALL = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(EQUALS, LT, LTE, GT, GTE)));

    @Getter
    final Method method;

    @Getter
    final BinaryOperator<CharSequence> stringify;

    ComparisonOperator(Method method, java.util.function.BinaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> boolean test(E e1, E e2) {
        return (Boolean) getMethod().invoke(e1, e2);
    }

    @SneakyThrows
    public static Method getBinaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }
}
