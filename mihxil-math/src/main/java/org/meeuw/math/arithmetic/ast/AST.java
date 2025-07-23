package org.meeuw.math.arithmetic.ast;

import java.util.*;
import java.util.stream.*;

import org.meeuw.math.abstractalgebra.*;
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
    public static <E extends FieldElement<E>> Stream<Expression<E>> stream(
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

    public static <E extends FieldElement<E>>  Expression<E> parse(String parse, Field<E> field) {
         InfixParser<E> parser = new InfixParser<>(parse, field);
        return parser.parse();
    }



    static class InfixParser<E extends FieldElement<E>> {

        private final Map<String, AlgebraicBinaryOperator> operators;
        private final String ops;
        private final List<String> tokens;
        private int pos = 0;
        private final Field<E> field;
        private final ExpressionField<E> expressionField;


        InfixParser(String input,  Field<E> field) {
            this.operators = field.getSupportedOperators()
                .stream()
                .collect(
                    Collectors.toMap(AlgebraicBinaryOperator::getSymbol, o -> o));
            this.field = field;
            this.expressionField = ExpressionField.of(field);
            this.ops = "()" + String.join("", this.operators.keySet());
            this.tokens = tokenize(input);
        }

        public Expression<E> parse() {
            Expression<E> expr = parseExpression();
            if (pos < tokens.size()) throw new IllegalArgumentException("Unexpected token: " + tokens.get(pos));
            return expr;
        }

        private Expression<E> parseExpression() {
            Expression<E> left = parseTerm();
            while (pos < tokens.size() && operators.containsKey(tokens.get(pos))) {
                String op = tokens.get(pos++);
                Expression<E> right = parseTerm();
                left = new BinaryOperation<>(operators.get(op), left, right);
            }
            return left;
        }

        private Expression<E> parseTerm() {
            String token = tokens.get(pos);
            if (token.equals("(")) {
                pos++;
                Expression<E> expr = parseExpression();
                if (!tokens.get(pos).equals(")")) throw new IllegalArgumentException("Expected ')'");
                pos++;
                return expr;
            } else {
                pos++;
                return new Value<>(field.parse(token));
            }
        }

        private List<String> tokenize(String input) {
            final List<String> result = new ArrayList<>();
            final StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    continue;
                }
                if (ops.indexOf(c) >= 0) {
                    if (!sb.isEmpty()) {
                        result.add(sb.toString());
                        sb.setLength(0);
                    }
                    result.add(String.valueOf(c));
                } else {
                    sb.append(c);
                }
            }
            if (!sb.isEmpty()) {
                result.add(sb.toString());
            }
            return result;
        }
    }


}
