package org.meeuw.math.arithmetic.ast;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

/**
 * Utilities related to Abstract Syntax trees of {@link AlgebraicElement}s and (at least for now) {@link AlgebraicBinaryOperator}s
 *
 * @since 0.19
 */
public class AST {


    /**
     * Return a stream of all possible AST expression with given leaves and operators
     * @param leaves
     * @param operators
     */
    public static <E extends AlgebraicElement<E>> Stream<Expression<E>> stream(
        List<E> leaves, Collection<AlgebraicBinaryOperator> operators) {
        if (leaves.size() == 1) {
            return Stream.of(new Value<>(leaves.get(0)));
        }
        return IntStream.range(1, leaves.size())
            .boxed()
            .flatMap(i -> {
                List<E> leftLeaves = leaves.subList(0, i);
                List<E> rightLeaves = leaves.subList(i, leaves.size());
                return stream(leftLeaves, operators)
                    .flatMap(left -> stream(rightLeaves, operators)
                        .flatMap(right -> operators.stream()
                            .map(op -> new BinaryOperation<>(op, left, right)))
                    );
            });
    }


    /**
     * Given an AST Expressin, convert it to infix notation (using brackets)
     */
    public static String toInfix(Expression<?> expr) {
        if (expr instanceof Value) {
            return expr.toString();
        } else if (expr instanceof BinaryOperation<?> binOp) {
            String left = toInfix(binOp.getLeft());
            String right = toInfix(binOp.getRight());
            return "(" + binOp.getOperator().stringify(left, right) + ")";
        }
        throw new IllegalArgumentException("Unknown Expression type");
    }
}
