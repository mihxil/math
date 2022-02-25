package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

import org.meeuw.math.numbers.Sizeable;

import static org.meeuw.math.text.TextUtils.overLine;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public enum UnaryOperator implements AlgebraicUnaryOperator {

    IDENTIFY(getUnaryOperatorMethod(AlgebraicElement.class, "self"), (s) -> "-" + s),

    NEGATION(getUnaryOperatorMethod(AdditiveGroupElement.class, "negation"), (s) -> "-" + s),

    RECIPROCAL(getUnaryOperatorMethod(MultiplicativeGroupElement.class, "reciprocal"),(s) -> s + superscript(-1)),

    INVERSION(getUnaryOperatorMethod(GroupElement.class, "inverse"),(s) -> s + superscript(-1)),

    SQR(getUnaryOperatorMethod(MultiplicativeGroupElement.class, "sqr"), (s) -> s + superscript(2)),

    ABS(getUnaryOperatorMethod(Sizeable.class, "abs"), (s) -> "|" + s + "|"),

    SQRT(getUnaryOperatorMethod(CompleteFieldElement.class, "sqrt"), (s) -> "√" + overLine(s)),

    SIN(getUnaryOperatorMethod(CompleteFieldElement.class, "sin"), (s) -> "sin(" + s + ")"),

    COS(getUnaryOperatorMethod(CompleteFieldElement.class, "cos"), (s) -> "cos(" + s + ")")
    ;

    @Getter
    final Method method;

    @Getter
    final java.util.function.UnaryOperator<CharSequence> symbol;

    UnaryOperator(Method method, java.util.function.UnaryOperator<CharSequence> symbol) {
        this.method = method;
        this.symbol = symbol;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e) {
        return (E) getMethod().invoke(e);
    }

    @SneakyThrows
    public static Method getUnaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name);
    }
}
