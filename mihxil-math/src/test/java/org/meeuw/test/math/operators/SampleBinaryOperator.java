package org.meeuw.test.math.operators;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

public class SampleBinaryOperator implements AlgebraicBinaryOperator {

    public static SampleBinaryOperator INSTANCE = new SampleBinaryOperator();

    private SampleBinaryOperator() {

    }
    @Override
    public <E extends AlgebraicElement<E>> E apply(E arg1, E arg2) {
        return arg1;
    }

    @Override
    public String stringify(String element1, String element2) {
        return "FIRST " + element1 + ", " + element2;
    }

    @Override
    public int precedence() {
        return 0;
    }

    @Override
    public String name() {
        return "first";
    }
}
