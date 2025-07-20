package org.meeuw.test.math.arithmetic.ast;

import org.junit.jupiter.api.Test;

import org.meeuw.math.Equivalence;
import org.meeuw.math.arithmetic.ast.*;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.math.uncertainnumbers.field.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

class ASTTest {

    @Test
    public void test() {
        BinaryOperation<UncertainReal> op = new BinaryOperation<>(
            MULTIPLICATION,
            new Value<>(exactly(2d)),
            new UnaryOperator<>(BasicAlgebraicUnaryOperator.SQR,
                new BinaryOperation<>(ADDITION,
                    new Value<>(exactly(3d)), new Value<>(exactly(8d))
                )
            )
        );
            assertThat(op.eval().getValue()).isEqualTo(2 * Math.pow(3 + 8, 2));
    }
   /* @Test
    public void equivalence() {
        Equivalence<Expression<UncertainReal>> equivalence = AST.equivalence(UncertainRealField.INSTANCE);

        assertThat(equivalence.test(new Value<>(exactly(2d)), new Value<>(exactly(2d)))).isTrue();

        assertThat(equivalence.test(
            new BinaryOperation<>(MULTIPLICATION,
                new Value<>(exactly(2d)), new Value<>(exactly(3d))
            ),
            new BinaryOperation<>(MULTIPLICATION,
                new Value<>(exactly(3d)), new Value<>(exactly(2d))
            ))).isTrue();
        assertThat(equivalence.test(
            new BinaryOperation<>(DIVISION,
                new Value<>(exactly(2d)), new Value<>(exactly(3d))
            ),
            new BinaryOperation<>(DIVISION,
                new Value<>(exactly(3d)), new Value<>(exactly(2d))
            ))).isFalse();

    }
*/
}
