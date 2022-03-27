package org.meeuw.math.abstractalgebra.integers;

import lombok.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.OperationException;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;


/**
 *  Integer types that can be positive can also define the factorial operator.
 */
public interface Factoriable<F extends MultiplicativeMonoidElement<F>>  {


    class InvalidFactorial extends OperationException {

        public InvalidFactorial(String s) {
            super(s);
        }
    }
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

    class Configuration implements ConfigurationAspect {
        @Getter
        @With
        private final Long maxArgument;

        public Configuration() {
            this(50000L);
        }


        @lombok.Builder
        private Configuration(Long maxArgument) {
            this.maxArgument = maxArgument;
        }

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.singletonList(Factoriable.class);
        }
    }

}
