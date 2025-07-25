package org.meeuw.math.test;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.complex.GaussianRationals;
import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
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
public  class Solver<E extends FieldElement<E>> {

    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    );

    private final AtomicLong tries = new AtomicLong();

    @Getter
    private final Field<E> structure;

    public Solver(Field<E> structure) {
        this.structure = structure;
    }

    public Stream<Expression<E>> stream(E... set) {
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

    public  static <E extends FieldElement<?>> SolverResult solve(String resultString, String[] numbers) {
        if (resultString.contains("i") || Stream.of(numbers).anyMatch(s -> s.contains("i"))) {
            return solve(GaussianRationals.INSTANCE, resultString, numbers);
        } else {
            return solve(RationalNumbers.INSTANCE, resultString, numbers);
        }

    }

    /**
     *
     */
    public  static <E extends FieldElement<E>> SolverResult solve(Field<E> structure, String resultString, String[] numbers) {

        ParseResult<E> parseResult = ParseResult.parse(structure, resultString, numbers);
        return solve(parseResult);
    }

    public  static <E extends FieldElement<E>> SolverResult solve(ParseResult<E> parseResult) {

        Solver<E> solver = new Solver<>(parseResult.field);
        AtomicLong matches = new AtomicLong();
        return new SolverResult(solver.evaledStream(parseResult.input)
            .filter(e ->
                e.result().eq(parseResult.result)
            ).peek(e -> matches.getAndIncrement())
            .map(EvaluatedExpression::toString),
            solver.tries, matches, parseResult.field);
    }

    public record SolverResult(Stream<String> stream, AtomicLong tries, AtomicLong matches, Field<?> field) {

    }

    public record ParseResult<E extends FieldElement<E>>(
        E result,
        E[] input,
        Field<E> field,
        String resultError,
        String inputError) {

        public static <F extends FieldElement<F>> ParseResult<F> parse(Field<F> field, String resultString, String... input) {
            String resultError = null;
            F  result;
            try {
                result = field.parse(resultString);
            } catch (NotParsable pe) {
                result = null;
                resultError = pe.getMessage();
            }
            String inputError = null;
            F[] set = field.newArray(input.length);
            try {
                for (int i = 0; i < set.length; i++) {
                    set[i] = field.parse(input[i]);
                }
            } catch (NotParsable pe) {
                inputError = pe.getMessage();
            }
            return new ParseResult<>(result, set, field, resultError, inputError);
        }

        public static ParseResult<?> parse(String resultString, String... input) {
            if (resultString.contains("i") || Stream.of(input).anyMatch(s -> s.contains("i"))) {
                return parse(GaussianRationals.INSTANCE, resultString, input);
            } else {
                return parse(RationalNumbers.INSTANCE, resultString, input);
            }
        }

    }

    public static void main(String[] integers) {
        if (integers.length < 3) {
            System.out.println();
            System.exit(1);
        }

        ParseResult<?> parseResult = ParseResult.parse(integers[0], Arrays.copyOfRange(integers, 1, integers.length));
        SolverResult result = Solver.solve(parseResult);
        result.stream().forEach(System.out::println);
        System.out.println("ready, found " + result.matches().get() + ", tried " + result.tries.get() + ", field " + result.field().toString());
    }
}
