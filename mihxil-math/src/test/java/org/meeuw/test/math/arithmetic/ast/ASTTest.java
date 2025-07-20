package org.meeuw.test.math.arithmetic.ast;

import org.junit.jupiter.api.Test;

import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.INSTANCE;

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

    @Test
    public void canonize() {
        assertThat(new BinaryOperation<>(ADDITION,
            new Value<>(exactly(3d)),
            new Value<>(exactly(2d))
            ).canonize(INSTANCE).toString()).isEqualTo("2 + 3");
    }

    @Test
    public void equivalence() {


        assertThat(new Value<>(exactly(2d)).canonize(INSTANCE)).isEqualTo(new Value<>(exactly(2d)));

        assertThat(new BinaryOperation<>(MULTIPLICATION,
            new Value<>(exactly(2d)),
            new Value<>(exactly(3d))
            ).canonize(INSTANCE)).isEqualTo(
            new BinaryOperation<>(MULTIPLICATION,
                new Value<>(exactly(3d)),
                new Value<>(exactly(2d))
            ).canonize(INSTANCE));
        assertThat(
            new BinaryOperation<>(DIVISION,
                new Value<>(exactly(2d)), new Value<>(exactly(3d))
            ).canonize(INSTANCE)).isNotEqualTo(
            new BinaryOperation<>(DIVISION,
                new Value<>(exactly(3d)), new Value<>(exactly(2d))
            ).canonize(INSTANCE));
    }
}
