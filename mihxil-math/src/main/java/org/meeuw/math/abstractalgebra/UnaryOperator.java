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

    NEGATION(getUnaryOperator(AdditiveGroupElement.class, "negation"), (s) -> "-" + s),

    RECIPROCAL(getUnaryOperator(MultiplicativeGroupElement.class, "reciprocal"),(s) -> s + TextUtils.superscript("-1")),

    SQR(getUnaryOperator(MultiplicativeGroupElement.class, "sqr"), (s) -> s + TextUtils.superscript("2"))

    ;

    @Getter
    final Method method;

    @Getter
    final java.util.function.UnaryOperator<CharSequence> symbol;

    UnaryOperator(Method method, java.util.function.UnaryOperator<CharSequence> symbol) {
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
