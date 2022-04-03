package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.test.math.sample.TestGroup;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class AdditiveGroupTest {

    @Test
    public void additiveGroupOperators() {
        assertThat(TestGroup.instance.getSupportedOperators())
            .contains(BasicAlgebraicBinaryOperator.ADDITION, BasicAlgebraicBinaryOperator.SUBTRACTION);

    }
    @Test
    public void additiveGroupUnaryOperators() {
        assertThat(TestGroup.instance.getSupportedUnaryOperators())
            .contains(BasicAlgebraicUnaryOperator.NEGATION);
    }

}
