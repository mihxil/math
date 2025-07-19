package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.arithmetic.ast.AST;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.exceptions.MathException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

/**
 * A tool to evaluate all possible expressions (of a certain number of rational numbers) (and check if it equals a certain value)
 */
public  class Solver {

    static final AlgebraicBinaryOperator[] operators = {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION
    };
    AtomicLong tries = new AtomicLong();

    public Stream<Expression<RationalNumber>> stream(RationalNumber... set) {
        PermutationGroup permutations = PermutationGroup.ofDegree(set.length);

        return permutations.stream().flatMap(permutation -> {
                RationalNumber[] permuted = permutation.permute(set);
                return AST.stream(List.of(permuted), List.of(operators));
            })
            .distinct()
            .peek(e -> {
                tries.getAndIncrement();
            });
    }


    public record EvaledExpression(Expression<RationalNumber> expression, RationalNumber result) {

        @Override
        public String toString() {
            return AST.toInfix(expression) + " = " + result;
        }
    }


    public Stream<EvaledExpression> evaledStream(RationalNumber... set) {
        return stream(set)
            .map(e -> {
                try {
                    RationalNumber evaled = e.eval();
                    return new EvaledExpression(e, evaled);
                } catch (MathException ex) {
                    return null;
                }
            })
            .filter(Objects::nonNull);
    }



    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        RationalNumber result = RationalNumber.of(Long.parseLong(integers[0]));
        RationalNumber[] set = new RationalNumber[integers.length - 1];
        for (int i = 1; i <= set.length; i++) {
            set[i - 1] = RationalNumber.of(Long.parseLong(integers[i]));
        }
        Solver solver = new Solver();
        solver.evaledStream(set)
            .filter(e ->
                e.result().eq(result)
            ).forEach(System.out::println);

    }
}
