package org.meeuw.math.abstractalgebra.integers;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;


/**
 *  Integer types that can be positive can also define the factorial operator.
 */
public interface Factoriable<F extends MultiplicativeMonoidElement<F>>  {


    AlgebraicUnaryOperator FACT = new AlgebraicUnaryOperator() {

        final Method method = BasicAlgebraicUnaryOperator.getUnaryOperatorMethod(Factoriable.class, "factorial");
        @SneakyThrows
        @Override
        public <E extends AlgebraicElement<E>> E apply(E e) {
            return (E) method.invoke(e);
        }

        @Override
        public String stringify(String element) {
            return element + "!";
        }

        @Override
        public String name() {
            return "factorial";
        }
    };

    F factorial();

}
