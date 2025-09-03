package org.meeuw.math.arithmetic.ast;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.arithmetic.ast.infix.InfixParser;
import org.meeuw.math.arithmetic.ast.infix.ParseException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.text.TextUtils;

/**
 * Utilities related to Abstract Syntax trees of {@link AlgebraicElement}s and (at least for now) {@link AlgebraicBinaryOperator}s
 *
 * @since 0.19
 */
public class AST {

    private AST() {

    }


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
     * Given an AST Expression, convert it to infix notation (using brackets)
     */
    public static String toInfix(Expression<?> expr) {
        return toInfix(expr, 0);
    }

    private static String toInfix(Expression<?> expr, int level) {
        if (expr instanceof Value<?> value) {
            return value.getValue().toString();
        } else if (expr instanceof Variable<?> variable) {
            return variable.getValue().get().toString();
        } else if (expr instanceof BinaryOperation<?> binOp) {
            String left = toInfix(binOp.getLeft(), level + 1);
            String right = toInfix(binOp.getRight(), level + 1);
            String stringify = binOp.getOperator().stringify(left, right);
            if (level ==0 ) {
                return stringify;
            } else {
                return "(" + stringify + ")";
            }
        } else if (expr instanceof UnaryOperation<?> unOp) {
            String operand = toInfix(unOp.getOperand(), level + 1);
            String stringify = unOp.getOperator().stringify(operand);
            if (level == 0) {
                return stringify;
            } else {
                return "(" + stringify + ")";
            }
        }
        throw new IllegalArgumentException("Unknown Expression type");
    }


    public static <E extends AlgebraicElement<E>>  Expression<E> parse(String parse, AlgebraicStructure<E> field)  {
        return parse(parse, field, (sign, name) ->
            field.getConstant(name).map(c -> {
                if (c instanceof AdditiveGroupElement<?> groupElement) {
                    if ("-".equals(sign)) {
                        return (E) groupElement.negation();

                    } else {
                        return c;
                    }
                } else {
                    assert sign == null;
                    return c;
                }
            })
        );
    }

    public static <E extends AlgebraicElement<E>>  Expression<E> parse(String parse, AlgebraicStructure<E> field, BiFunction<String, String, Optional<E>> getConstant)  {
        try {
            InfixParser<E> parser = new InfixParser<>(TextUtils.undo(parse), field, getConstant);
            return parser.parse();
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }




}
