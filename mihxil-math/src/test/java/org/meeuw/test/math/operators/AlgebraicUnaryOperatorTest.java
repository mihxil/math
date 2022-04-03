package org.meeuw.test.math.operators;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.test.math.sample.TestElement;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgebraicUnaryOperatorTest {

    @Test
    public void identity() {
        TestElement in = new TestElement(null);
        TestElement out = AlgebraicUnaryOperator.identity().apply(in);

        assertThat(in).isSameAs(out);

        assertThat(AlgebraicUnaryOperator.identity().stringify(in)).isEqualTo("self(testelement)");

        assertThat(AlgebraicUnaryOperator.identity().name()).isEqualTo("identity");

        assertThat(AlgebraicUnaryOperator.identity().andThen(BasicAlgebraicUnaryOperator.NEGATION).stringify("x")).isEqualTo("-self(x)");

        assertThat(AlgebraicUnaryOperator.identity().andThen(BasicAlgebraicUnaryOperator.NEGATION).name()).isEqualTo("identity and then negation");

    }
}
