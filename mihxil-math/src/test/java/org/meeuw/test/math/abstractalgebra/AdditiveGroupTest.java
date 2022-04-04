package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.test.math.sample.SampleStructure;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class AdditiveGroupTest {

    @Test
    public void additiveGroupOperators() {
        assertThat(new SampleStructure().getSupportedOperators())
            .contains(BasicAlgebraicBinaryOperator.ADDITION, BasicAlgebraicBinaryOperator.SUBTRACTION);

    }
    @Test
    public void additiveGroupUnaryOperators() {
        assertThat(new SampleStructure().getSupportedUnaryOperators())
            .contains(BasicAlgebraicUnaryOperator.NEGATION);
    }

}
