package org.meeuw.math.test;

import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.ComplexNumbers;
import org.meeuw.math.abstractalgebra.complex.GaussianRationals;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.arithmetic.ast.AST;
import org.meeuw.math.arithmetic.ast.Expression;


public class Calculator {


   // tag::eval[]

    public static String eval(String expression, String field) {
        Field<?> f = switch (field) {
            case "rational" -> RationalNumbers.INSTANCE;
            case "real" -> RealField.INSTANCE;
            case "bigdecimal" -> BigDecimalField.INSTANCE;
            case "gaussian" -> GaussianRationals.INSTANCE;
            case "complex" -> ComplexNumbers.INSTANCE;
            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        };
        Expression<?> parsedExpression = AST.parseInfix(expression, f);
        return parsedExpression.eval().toString();
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        System.out.println(arg + " = " + eval(arg, f));
    }
}
