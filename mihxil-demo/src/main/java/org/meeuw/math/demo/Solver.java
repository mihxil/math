package org.meeuw.math.demo;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.abstractalgebra.complex.GaussianRationals;
import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.abstractalgebra.quaternions.Quaternions;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.arithmetic.ast.*;
import org.meeuw.math.exceptions.MathException;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;

/**
 * A tool to evaluate all possible expressions (of a certain number of rational numbers) (and check if it equals a certain value)
 */
public  class Solver<E extends RingElement<E>> {

    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    );

    private final AtomicLong tries = new AtomicLong();

    @Getter
    private final Ring<E> structure;

    public Solver(Ring<E> structure) {
        this.structure = structure;
    }

    @SafeVarargs
    public final Stream<Expression<E>> stream(E... set) {
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
            .map( e -> e.canonize(structure))
            .distinct()
            .peek(e -> tries.getAndIncrement());
    }



    public Stream<EvaluatedExpression<E>> evaledStream(E... set) {
        return stream(set)
            .map(e -> {
                try {
                    E evaled = e.eval();
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
    public  static <E extends RingElement<E>> SolverResult solve(Ring<E> structure, String outcomeString, String inputStrings) {

        ParseResult<E> outcome = parseOutcome(structure, outcomeString);
        ParseResult<E[]> input = parseInput(structure, inputStrings);
        if (outcome.success() && input.success()) {
            return solve(structure, outcome.result(), input.result());
        } else {
            throw new NotParsable(outcome.error() + "/" + input.error());
        }
    }

    public  static <E extends RingElement<E>> SolverResult solve(Ring<E> structure, E outcome, E[] input) {

        Solver<E> solver = new Solver<>(structure);
        AtomicLong matches = new AtomicLong();
        return new SolverResult(solver.evaledStream(input)
            .filter(e ->
                e.result().eq(outcome)
            ).peek(e -> matches.getAndIncrement())
            .map(EvaluatedExpression::toString),
            solver.tries, matches, structure);
    }

    public static <F extends RingElement<F>> ParseResult<F> parseOutcome(Ring<F> field, String outcomeString) {
        String resultError = null;
        F result;
        try {
            result = field.fromString(outcomeString);
        } catch (NotParsable pe) {
            result = null;
            resultError = pe.getMessage();
        }
        return new ParseResult<F>(outcomeString, result, resultError);
    }
    public static <F extends RingElement<F>> ParseResult<F[]> parseInput(Ring<F> field, String inputStrings) {
        String inputError = null;

        String[] input = inputStrings.split("\\s+");
        F[] set = field.newArray(input.length);
        try {
            for (int i = 0; i < set.length; i++) {
                set[i] = field.fromString(input[i]);
            }
        } catch (NotParsable pe) {
            inputError = pe.getMessage();
        }
        return new ParseResult<>(inputStrings, set, inputError);
    }

    public static Ring<?> algebraicStructureFor(String outcomeString, String input) {
        if (outcomeString.matches(".*[jk].*") || input.matches(".*[jk].*")) {
            return Quaternions.of(RationalNumbers.INSTANCE);
        } else if (outcomeString.contains("i") || input.contains("i")) {
            return GaussianRationals.INSTANCE;
        } else {
            return RationalNumbers.INSTANCE;
        }
    }


    public record SolverResult(Stream<String> stream, AtomicLong tries, AtomicLong matches, Ring<?> field) {


    }

    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        String resultString = integers[0];
        String inputStrings = String.join(" ", Arrays.copyOfRange(integers, 1, integers.length));

        Ring<?> field = algebraicStructureFor(resultString, inputStrings);
        SolverResult solverResult = Solver.solve(field, resultString, inputStrings);
        solverResult.stream().forEach(System.out::println);
        System.out.println("ready, found " + solverResult.matches().get() + ", tried " + solverResult.tries.get() + ", field " + solverResult.field().toString());
    }
}
