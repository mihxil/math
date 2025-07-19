package org.meeuw.test.math.arithmetic.ast;

import org.junit.jupiter.api.Test;

import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.MULTIPLICATION;

class ASTTest {

    @Test
    public void test() {
        BinaryOperation<UncertainReal> op = new BinaryOperation<>(
            MULTIPLICATION,
            new Value<>(UncertainDoubleElement.exactly(2d)),
            new UnaryOperator<>(BasicAlgebraicUnaryOperator.SQR,
                new BinaryOperation<>(ADDITION,
                    new Value<>(UncertainDoubleElement.exactly(3d)), new Value<>(UncertainDoubleElement.exactly(8d))
                )
            )
        );
            assertThat(op.eval().getValue()).isEqualTo(2 * Math.pow(3 + 8, 2));
    }

}
