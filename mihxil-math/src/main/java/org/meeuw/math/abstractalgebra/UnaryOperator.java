package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum UnaryOperator implements AlgebraicUnaryOperator {

    NEGATION(getUnaryOperator(AdditiveGroupElement.class, "negation"), "-"),
    RECIPROCAL(getUnaryOperator(MultiplicativeGroupElement.class, "reciprocal"), TextUtils.superscript("-1"))

    ;

    @Getter
    final Method method;

    @Getter
    final String symbol;

    UnaryOperator(Method method, String symbol) {
        this.method = method;
        this.symbol = symbol;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e) {
        return (E) getMethod().invoke(e);
    }

    @SneakyThrows
    public static Method getUnaryOperator(Class<?> clazz, String name) {
        return clazz.getMethod(name);
    }
}
