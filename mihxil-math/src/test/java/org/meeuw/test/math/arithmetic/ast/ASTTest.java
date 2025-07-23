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
        BinaryOperation<UncertainReal> op = new Value<>(exactly(2d)).times(
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
        assertThat(
            new Value<>(exactly(3d))
                .plus(new Value<>(exactly(2d))
            ).canonize(INSTANCE).toString()).isEqualTo("2 + 3");


        var sub=
            new Value<>(exactly(3d)).minus(new Value<>(exactly(8d)));

        var prod1 = sub.times( new Value<>(exactly(3)));
        var prod2 = prod1.reverse();

        System.out.println(AST.toInfix(prod2.canonize(INSTANCE)));
        System.out.println(AST.toInfix(prod2));
        System.out.println(AST.toInfix(prod2.reverse()));
        System.out.println(AST.toInfix(prod2.reverse().canonize(INSTANCE)));

        assertThat(
            prod2.canonize(INSTANCE))
            .isEqualTo(prod1.canonize(INSTANCE));

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

    @Test
    public void parse() {
        Expression<UncertainReal> exp = AST.parse("(3 ⋅ (8 - 3)) + 8", INSTANCE);
        System.out.println(AST.toInfix(exp));
        Expression<UncertainReal> exp2 = AST.parse("((8 - 3) ⋅ 3) + 8", INSTANCE);
        System.out.println(AST.toInfix(exp2.canonize(INSTANCE)));

    }
}
