package org.meeuw.test.math.arithmetic.ast;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.INSTANCE;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

class ASTTest {

    @Test
    public void test() {
        BinaryOperation<RealNumber> op = new Value<>(exactly(2d)).times(
            new UnaryOperation<>(BasicAlgebraicUnaryOperator.SQR,
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
            ).canonize(INSTANCE).toString()).isEqualTo("(binary:(value:2) + (value:3))");


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
    public void parseInfix() {
        Expression<RealNumber> exp = AST.parse("(3 * (8 - 3)) + 8", INSTANCE);
        System.out.println(AST.toInfix(exp.canonize(INSTANCE)));
        Expression<RealNumber> exp2 = AST.parse("((8 - 3) * 3) + 8", INSTANCE);
        System.out.println(AST.toInfix(exp2.canonize(INSTANCE)));
    }
    @Test
    public void parseInfix1(){
        String s = "(1 + 2) + 200";
        Expression<?> parse = AST.parse(s, INSTANCE);
        assertThat(parse).isInstanceOf(BinaryOperation.class);
    }

    @Test
    public void parseInfix2(){
        String s = "1 + -1";
        Expression<RealNumber> parse = AST.parse(s, INSTANCE);
        assertThat(parse).isInstanceOf(BinaryOperation.class);
    }

    @Test
    public void stream() {
        assertThat(AST.stream(List.of(INSTANCE.element(1), INSTANCE.element(2)),
            List.of(
                ADDITION,MULTIPLICATION)
        ).toList().toString()).isEqualTo("[(binary:(value:1) + (value:2)), (binary:(value:1) ⋅ (value:2))]");
    }
}
