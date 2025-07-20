package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.exceptions.MathException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

/**
 * A tool to evaluate all possible expressions (of a certain number of rational numbers) (and check if it equals a certain value)
 */
public  class Solver {

    private static final RationalNumbers STRUCTURE = RationalNumbers.INSTANCE;
    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    );

    private final AtomicLong tries = new AtomicLong();

    public Stream<Expression<RationalNumber>> stream(RationalNumber... set) {
        PermutationGroup permutations = PermutationGroup.ofDegree(set.length);

        return permutations.stream()
            .map(permutation -> permutation.permute(set))
            .map(List::of)
            .distinct()
            .flatMap(permuted ->
                AST.stream(
                    permuted,
                    OPERATORS
                )
            )
            .map( e -> e.canonize(STRUCTURE))
            .distinct()
            .peek(e -> tries.getAndIncrement());
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
        RationalNumber result = STRUCTURE.parse(resultString);
        RationalNumber[] set = new RationalNumber[numbers.length];
        for (int i = 0; i < set.length; i++) {
            set[i] = STRUCTURE.parse(numbers[i]);
        }
        Solver solver = new Solver();
        AtomicLong matches = new AtomicLong();
        return new SolverResult(solver.evaledStream(set)
            .filter(e ->
                e.result().eq(result)
            ).peek(e -> matches.getAndIncrement())
            .map(EvaluatedExpression::toString),
            solver.tries, matches);
    }

    public record SolverResult(Stream<String> stream, AtomicLong tries, AtomicLong matches) {

    }


    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        SolverResult result = result(integers[0], Arrays.copyOfRange(integers, 1, integers.length));
        result.stream().forEach(System.out::println);
        System.out.println("ready found " + result.matches().get() + ", tried " + result.tries.get() );
    }
}
