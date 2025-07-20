package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
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

        return permutations.stream()
            .flatMap(permutation -> {
                RationalNumber[] permuted = permutation.permute(set);
                return AST.stream(
                    List.of(permuted),
                    List.of(operators)
                );
            })
            .distinct()
            .peek(e -> {
                tries.getAndIncrement();
            });
    }


    public record EvaledExpression(Expression<RationalNumber> expression, RationalNumber result) {

        @Override
        @NonNull
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


    public static Stream<String> result(String resultString, String[] numbers) {
        RationalNumbers instance = RationalNumbers.INSTANCE;
        RationalNumber result = instance.parse(resultString);
        RationalNumber[] set = new RationalNumber[numbers.length];
        for (int i = 0; i < set.length; i++) {
            set[i] = instance.parse(numbers[i]);
        }
        Solver solver = new Solver();
        return solver.evaledStream(set)
            .filter(e ->
                e.result().eq(result)
            ).map(EvaledExpression::toString);
    }

    public static String[] resultList(String resultString, String[] numbers) {

        return result(resultString, numbers)
            .toArray(String[]::new);
    }

    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        result(integers[0], Arrays.copyOfRange(integers, 1, integers.length))
         .forEach(System.out::println);
        System.out.println("ready");

    }
}
