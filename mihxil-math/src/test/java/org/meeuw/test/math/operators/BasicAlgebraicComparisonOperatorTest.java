package org.meeuw.test.math.operators;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicComparisonOperator;
import org.meeuw.test.math.sample.SampleElement;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAlgebraicComparisonOperatorTest {

    @Test
    public void test() {
        SampleElement e1 = new SampleElement();
        SampleElement e2 = new SampleElement();
        assertThat(BasicComparisonOperator.EQ.test(e1, e2)).isFalse();
        assertThat(BasicComparisonOperator.EQ.test(e1, e1)).isTrue();

        assertThat(BasicComparisonOperator.EQ.stringify("a", "b")).isEqualTo("a ≈ b");

        assertThat(BasicComparisonOperator.EQ.getSymbol()).isEqualTo("≈");
    }



}
