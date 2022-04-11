package org.meeuw.math.operators;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

public interface OperatorInterface {

    Comparator<OperatorInterface> COMPARATOR = Comparator
        .comparing(OperatorInterface::ordinal)
        .thenComparing(OperatorInterface::name);

    String name();

    default int ordinal() {
        return Integer.MAX_VALUE;
    }

   Method getMethod();

    @SneakyThrows
    default <E extends AlgebraicElement<E>> boolean isAlgebraicFor(E e) {
        Method m = e.getClass().getMethod(getMethod().getName(), getMethod().getParameterTypes());
        return m.getAnnotation(NonAlgebraic.class) == null;
    }
}
