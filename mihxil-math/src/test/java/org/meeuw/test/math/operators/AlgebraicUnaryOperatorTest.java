package org.meeuw.test.math.operators;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.test.math.sample.SampleElement;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgebraicUnaryOperatorTest {

    @Test
    public void identity() {
        SampleElement in = new SampleElement();
        SampleElement out = AlgebraicUnaryOperator.identity().apply(in);

        assertThat(in).isSameAs(out);

        assertThat(AlgebraicUnaryOperator.identity().stringify(in)).isEqualTo("self(sampleelement)");

        assertThat(AlgebraicUnaryOperator.identity().name()).isEqualTo("identity");
    }

    @Test
    public void andThen() {
        assertThat(AlgebraicUnaryOperator.identity().andThen(BasicAlgebraicUnaryOperator.NEGATION).stringify("x")).isEqualTo("-self(x)");

        assertThat(AlgebraicUnaryOperator.identity().andThen(BasicAlgebraicUnaryOperator.NEGATION).name()).isEqualTo("identity and then NEGATION");
    }

    @Test
    public void compose() {
        assertThat(AlgebraicUnaryOperator.identity().compose(BasicAlgebraicUnaryOperator.NEGATION).name()).isEqualTo("NEGATION and then identity");
    }


}
