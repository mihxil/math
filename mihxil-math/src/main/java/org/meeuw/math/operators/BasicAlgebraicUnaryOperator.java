package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.*;

import org.meeuw.math.exceptions.NoSuchOperatorException;

import static org.meeuw.math.text.TextUtils.overLine;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * The predefined  basic 'unary operators' of algebra's.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public enum BasicAlgebraicUnaryOperator implements AlgebraicUnaryOperator {

    IDENTIFY(getUnaryOperatorMethod(AlgebraicElement.class, "self"), (s) -> s.length() > 0 && s.charAt(0) == '+' ? s : "+" + s),

    NEGATION(getUnaryOperatorMethod(AdditiveGroupElement.class, "negation"), (s) -> s.length() > 0 && s.charAt(0) == '-' ? "+" + s.subSequence(1, s.length()) : "-" + s),

    RECIPROCAL(getUnaryOperatorMethod(MultiplicativeGroupElement.class, "reciprocal"), (s) -> s + superscript(-1)),

    INVERSION(getUnaryOperatorMethod(GroupElement.class, "inverse"),(s) -> "inverse(" + s  + ")"),

    SQR(getUnaryOperatorMethod(MultiplicativeSemiGroupElement.class, "sqr"), (s) -> s + superscript(2)),

    SQRT(getUnaryOperatorMethod(CompleteFieldElement.class, "sqrt"), (s) -> "√" + overLine(s)),

    SIN(getUnaryOperatorMethod(CompleteFieldElement.class, "sin"), (s) -> "sin(" + s + ")"),

    COS(getUnaryOperatorMethod(CompleteFieldElement.class, "cos"), (s) -> "cos(" + s + ")"),

    EXP(getUnaryOperatorMethod(CompleteFieldElement.class, "exp"), (s) -> "exp(" + s + ")"),

    LN(getUnaryOperatorMethod(CompleteFieldElement.class, "ln"), (s) -> "ln(" + s + ")"),

    SINH(getUnaryOperatorMethod(CompleteFieldElement.class, "sinh"), (s) -> "sinh(" + s + ")"),

    COSH(getUnaryOperatorMethod(CompleteFieldElement.class, "cosh"), (s) -> "cosh(" + s + ")")

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
            try {
                // It is possible that the operation is defined, but the class does not extend the correct class
                // e.g. an oddinteger implements negation, but it is not an additive group (negation is possible inside the algebra, but addition itself isn't).
                return (E) e.getClass().getMethod(method.getName()).invoke(e);
            } catch (java.lang.NoSuchMethodException noSuchMethodError) {
                throw new NoSuchOperatorException("No operation " + this + " found on " + e, noSuchMethodError);
            }
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @SneakyThrows
    public static Method getUnaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getDeclaredMethod(name);
    }

    @Override
    public String stringify(String element1) {
        return stringify.apply(element1).toString();
    }
}
