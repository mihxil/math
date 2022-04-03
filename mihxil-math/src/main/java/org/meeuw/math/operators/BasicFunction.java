package org.meeuw.math.operators;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.meeuw.math.numbers.Sizeable;
import org.meeuw.math.numbers.SizeableScalar;

import static org.meeuw.math.ReflectionUtils.getUnaryOperatorMethod;

@Log
public enum BasicFunction implements GenericFunction {

    ABS(getUnaryOperatorMethod(Sizeable.class, "abs"), (s) -> "|" + s + "|"),

    DECIMAL(getUnaryOperatorMethod(SizeableScalar.class, "bigDecimalValue"), (s) -> "decimal(" + s + ")"),
    INTEGER(getUnaryOperatorMethod(SizeableScalar.class, "bigIntegerValue"), (s) -> "integer(" + s + ")");

    @Getter
    final Method method;

    final java.util.function.UnaryOperator<CharSequence> stringify;

    BasicFunction(Method method, java.util.function.UnaryOperator<CharSequence> stringify) {
        this.method = method;
        this.stringify = stringify;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Override
    public <T, R> R apply(T t) {
        try {
            return (R) method.invoke(t);
        } catch (IllegalArgumentException iae) {
            log.fine(this + " on " + t + " but " + t.getClass() + " not a " + method.getDeclaringClass());
            return (R) t.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(t);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    @Override
    public String stringify(String element) {
        return stringify.apply(element).toString();
    }
}
