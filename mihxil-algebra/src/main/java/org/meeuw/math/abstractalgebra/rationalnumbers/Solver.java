package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.exceptions.MathException;

/**
 * A tool to evaluate all possible expressions (of a certain number of rational numbers) (and check if it equals a certain value)
 */
public  class Solver {

    AtomicLong tries = new AtomicLong();

    public Stream<Expression<RationalNumber>> stream(RationalNumber... set) {
        PermutationGroup permutations = PermutationGroup.ofDegree(set.length);

        return permutations.stream()
            .flatMap(permutation -> {
                RationalNumber[] permuted = permutation.permute(set);
                return AST.stream(
                    List.of(permuted),
                    RationalNumbers.INSTANCE.getSupportedOperators()
                );
            })
            .distinct()
            .peek(e -> {
                tries.getAndIncrement();
            });
    }



    public Stream<EvaluatedExpression<RationalNumber>> evaledStream(RationalNumber... set) {
        return stream(set)
            .map(e -> {
                try {
                    RationalNumber evaled = e.eval();
                    return new EvaluatedExpression<>(e, evaled);
                } catch (MathException ex) {
                    return null;
                }
            })
            .filter(Objects::nonNull);
    }


    /**
     *
     */
    public static SolverResult result(String resultString, String[] numbers) {
        RationalNumbers instance = RationalNumbers.INSTANCE;
        RationalNumber result = instance.parse(resultString);
        RationalNumber[] set = new RationalNumber[numbers.length];
        for (int i = 0; i < set.length; i++) {
            set[i] = instance.parse(numbers[i]);
        }
        Solver solver = new Solver();
        return new SolverResult(solver.evaledStream(set)
            .filter(e ->
                e.result().eq(result)
            ).map(EvaluatedExpression::toString),
            solver.tries);
    }

    public record SolverResult(Stream<String> stream, AtomicLong tries) {

    }


    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        SolverResult result = result(integers[0], Arrays.copyOfRange(integers, 1, integers.length));
        result.stream().forEach(System.out::println);
        System.out.println("ready tried " + result.tries.get());

    }
}
