package org.meeuw.math.arithmetic.ast;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.AlgebraicBinaryOperator;

public class Parser<E extends AlgebraicElement<E>, S extends AlgebraicStructure<E>> {


    private final S structure;

    public Parser(S structure) {
        this.structure = structure;
    }

    public Optional<AlgebraicBinaryOperator> parseBinaryOperator(String s) {
        return structure.getSupportedOperators().stream().filter(
            o -> s.trim().equalsIgnoreCase(o.getSymbol())).findFirst();

    }
    /**
     * Shunting yard algorithm for parsing a stream of
     */
    public Stream<String> tokenize(String s) {
        // Build a regex pattern for all operator symbols and parentheses
        String operatorSymbols = structure.getSupportedOperators().stream()
            .map(op -> "\\Q" + op.getSymbol() + "\\E")
            .reduce((a, b) -> a + "|" + b)
            .orElse("");
        String pattern = String.format("(%s|\\(|\\))", operatorSymbols);

        // Split and stream tokens
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile(pattern).matcher(s);
        int last = 0;
        while (m.find()) {
            if (m.start() > last) {
                String value = s.substring(last, m.start()).trim();
                if (!value.isEmpty()) tokens.add(value);
            }
            tokens.add(m.group());
            last = m.end();
        }
        if (last < s.length()) {
            String value = s.substring(last).trim();
            if (!value.isEmpty()) tokens.add(value);
        }
        return tokens.stream();
    }


    public Expression<E> parse(String tokens) {
        return parse(tokenize(tokens));
    }

    /**
     * Shunting yard algorithm for parsing a stream of tokens.
     */
    public Expression<E> parse(Stream<String> tokens) {
        Stack<AlgebraicBinaryOperator> operators = new Stack<>();
        Stack<Expression<E>> output = new Stack<>();

        tokens.forEach(token -> {
            Optional<AlgebraicBinaryOperator> operator = parseBinaryOperator(token);
            if (operator.isPresent()) {
                AlgebraicBinaryOperator op = operator.get();
                while (!operators.isEmpty() &&
                    operators.peek() != null &&
                    operators.peek().precedence() >= op.precedence()) {
                    popOperator(operators, output);
                }
                operators.push(op);
            } else if ("(".equals(token.trim())) {
                operators.push(null); // marker for '('
            } else if (")".equals(token.trim())) {
                while (!operators.isEmpty() && operators.peek() != null) {
                    popOperator(operators, output);
                }
                if (!operators.isEmpty() && operators.peek() == null) {
                    operators.pop(); // pop '('
                }
            } else {
                E e = structure.fromString(token);
                output.push(new Value<>(e));
            }
        });
        while (!operators.isEmpty()) {
            popOperator(operators, output);
        }
        return output.pop();
    }

    private void popOperator(Stack<AlgebraicBinaryOperator> operators, Stack<Expression<E>> output) {
        AlgebraicBinaryOperator op = operators.pop();
        Expression<E> right = output.pop();
        Expression<E> left = output.pop();
        BinaryOperation<E> expr = new BinaryOperation<>(op, left, right);
        output.push(expr);
    }

}
