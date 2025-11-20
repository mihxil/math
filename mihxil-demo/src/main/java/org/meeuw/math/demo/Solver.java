package org.meeuw.math.demo;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.meeuw.math.IntegerUtils;
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
@Log
public  class Solver<E extends RingElement<E>> {
    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    );

    private final AtomicLong tries = new AtomicLong();
    private final AtomicLong considered = new AtomicLong();

    @Getter
    private final Ring<E> structure;

    public Solver(Ring<E> structure) {
        this.structure = structure;

    }

    native void callBack(long considered, long tried, long total, Expression<E> expression);

    native boolean cancelled();

    @SafeVarargs
    public final Stream<Expression<E>> stream(E... set) {
        PermutationGroup permutations = PermutationGroup.ofDegree(set.length);
        long streamSize = permutations.getCardinality().getValue().longValue() * AST.streamSize(set.length, OPERATORS.size()).longValue();

        long dividor = Arrays.stream(set)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            )).values().stream()
             .map(count -> IntegerUtils.factorial(count.intValue()))
            .reduce(1L, (a, b) -> a * b);
        long total = streamSize / dividor;

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
            .peek((p) -> {
                if (cancelled()) {
                    throw new CancellationException("cancelled");
                }
            })
            .map( e -> e.canonize(structure))
            .peek(e -> {
                considered.incrementAndGet();
            })
            .distinct()
            .peek(e ->
                callBack(considered.get(), tries.incrementAndGet(), total, e)
            );
    }



    @SafeVarargs
    public final Stream<EvaluatedExpression<E>> evaledStream(E... set) {
        log.fine(() -> "evaling" + List.of(set));
        return stream(set)
            .map(e -> {
                try {
                    log.fine(() -> "Evaling " +  e);
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
    public SolverResult solve(String outcomeString, String inputStrings) {

        ParseResult<E> outcome = parseOutcome(outcomeString);
        ParseResult<E[]> input = parseInput(inputStrings);
        if (outcome.success() && input.success()) {
            SolverResult result = solve(outcome.result(), input.result());
            log.fine(() -> "solved: " + result);
            return result;
        } else {
            throw new NotParsable(outcome.error() + "/" + input.error());
        }
    }


    public SolverResult solve(E outcome, E[] input) {
        AtomicLong matches = new AtomicLong();
        log.fine(() -> "Solving input " + List.of(input) + " for " + outcome + " ( in field " + structure + ")");

        return new SolverResult(
            evaledStream(input)
            .filter(e ->
                e.result().eq(outcome)
            ).peek(e -> matches.getAndIncrement())
                .map(EvaluatedExpression::toString),
            tries, matches, structure
        );
    }


    public static <F extends RingElement<F>> ParseResult<F> parseOutcome(Ring<F> structure, String outcomeString) {
        log.fine(() -> "Parsing output " + outcomeString + " in field " + structure);

        String resultError = null;
        F result;
        try {
            result = structure.fromString(outcomeString);
        } catch (NotParsable pe) {
            result = null;
            resultError = pe.getMessage();
        }
        return new ParseResult<>(outcomeString, result, resultError);
    }
    public ParseResult<E> parseOutcome(String outcomeString) {
        return parseOutcome(this.structure, outcomeString);
    }

    public static <F extends RingElement<F>> ParseResult<F[]> parseInput(Ring<F> structure, String inputStrings) {
        log.fine(() -> "Parsing input " + inputStrings + " in field " + structure);

        String inputError = null;

        String[] input = inputStrings.split("\\s+");
        F[] set = structure.newArray(input.length);
        try {
            for (int i = 0; i < set.length; i++) {
                set[i] = structure.fromString(input[i]);
            }
        } catch (NotParsable pe) {
            inputError = pe.getMessage();
        }
        return new ParseResult<>(inputStrings, set, inputError);
    }
    public ParseResult<E[]> parseInput(String inputStrings) {
        return parseInput(this.structure, inputStrings);
    }


    public static Ring<?> algebraicStructureFor(String outcomeString, String input) {
        log.info(() -> "Determining algebraic structure for outcome " + outcomeString + " and input " + input);
        if (outcomeString.matches(".*[jk].*") || input.matches(".*[jk].*")) {
            return Quaternions.of(RationalNumbers.INSTANCE);
        } else if (outcomeString.contains("i") || input.contains("i")) {
            return GaussianRationals.INSTANCE;
        } else {
            return RationalNumbers.INSTANCE;
        }
    }

    public record SolverResult(Stream<String> stream, AtomicLong tries, AtomicLong matches, Ring<?> field) {
        /**
         * Calling stream().iterator() in cheerpj doesn't seem to work. I don't get it either.
         */
        public Iterator<String> iterator() {
            return stream().iterator();
        }

        public List<String> list() {
            return stream().toList();
        }
    }

    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }
        String resultString = integers[0];
        String inputStrings = String.join(" ", Arrays.copyOfRange(integers, 1, integers.length));

        Ring<?> field = algebraicStructureFor(resultString, inputStrings);

        Solver<?> solver = new Solver<>(field);
        SolverResult solverResult = solver.solve(resultString, inputStrings);
        solverResult.stream().forEach(System.out::println);
        System.out.println("ready, found " + solverResult.matches().get() + ", tried " + solverResult.tries.get() + ", field " + solverResult.field().toString());
    }
}
