package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.text.TextUtils.overLine;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public enum BasicAlgebraicUnaryOperator implements AlgebraicUnaryOperator {

    IDENTIFY(getUnaryOperatorMethod(AlgebraicElement.class, "self"), (s) -> "self(" + s + ")"),

    NEGATION(getUnaryOperatorMethod(AdditiveGroupElement.class, "negation"), (s) -> "-" + s),

    RECIPROCAL(getUnaryOperatorMethod(MultiplicativeGroupElement.class, "reciprocal"), (s) -> s + superscript(-1)),

    INVERSION(getUnaryOperatorMethod(GroupElement.class, "inverse"),(s) -> "inverse(" + s  + ")"),

    SQR(getUnaryOperatorMethod(MultiplicativeGroupElement.class, "sqr"), (s) -> s + superscript(2)),


    SQRT(getUnaryOperatorMethod(CompleteFieldElement.class, "sqrt"), (s) -> "√" + overLine(s)),

    SIN(getUnaryOperatorMethod(CompleteFieldElement.class, "sin"), (s) -> "sin(" + s + ")"),

    COS(getUnaryOperatorMethod(CompleteFieldElement.class, "cos"), (s) -> "cos(" + s + ")"),

    EXP(getUnaryOperatorMethod(CompleteFieldElement.class, "exp"), (s) -> "exp(" + s + ")"),

    LN(getUnaryOperatorMethod(CompleteFieldElement.class, "ln"), (s) -> "ln(" + s + ")"),

    SINH(getUnaryOperatorMethod(CompleteFieldElement.class, "sinh"), (s) -> "sinh(" + s + ")"),

    COSH(getUnaryOperatorMethod(CompleteFieldElement.class, "cosh"), (s) -> "cosh(" + s + ")"),


    ;

    @Getter
    final Method method;

    final java.util.function.UnaryOperator<CharSequence> stringify;

    BasicAlgebraicUnaryOperator(Method method, java.util.function.UnaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e) {
        try {
            return (E) method.invoke(e);
        } catch (IllegalArgumentException iae) {
            log.fine(this + " on " + e + " but " + e.getClass() + " not a " + method.getDeclaringClass());
            return (E) e.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(e);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @SneakyThrows
    public static Method getUnaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name);
    }

    @Override
    public String stringify(String element1) {
        return stringify.apply(element1).toString();
    }
}
