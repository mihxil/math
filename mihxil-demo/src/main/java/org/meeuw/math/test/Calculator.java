package org.meeuw.math.test;

import java.math.MathContext;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.arithmetic.ast.AST;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PARENTHESES;


public class Calculator {


   // tag::eval[]

    public static String eval(String expression, String field) {
        Field<?> f = switch (field) {
            case "rational" -> RationalNumbers.INSTANCE;
            case "real" -> RealField.INSTANCE;
            case "bigdecimal" -> BigDecimalField.INSTANCE;
            case "gaussian" -> GaussianRationals.INSTANCE;
            case "complex" -> ComplexNumbers.INSTANCE;
            case "bigcomplex" -> BigComplexNumbers.INSTANCE;

            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        };
        Expression<?> parsedExpression = AST.parseInfix(expression, f);
        try (ConfigurationService.Reset r = ConfigurationService.setConfiguration(cb ->
            cb.configure(UncertaintyConfiguration.class,
                    (ub) -> ub.withNotation(PARENTHESES))
                .configure(MathContextConfiguration.class,
                    (mc) ->
                        mc.withContext(new MathContext(Utils.PI.length())))
        )) {
            return parsedExpression.eval().toString();
        }
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        System.out.println(arg + " = " + eval(arg, f));
    }
}
