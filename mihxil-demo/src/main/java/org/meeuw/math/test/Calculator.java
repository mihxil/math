package org.meeuw.math.test;

import lombok.Getter;

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

import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;


public class Calculator {

    @Getter
    public  enum FieldInformation {
        rational(RationalNumbers.INSTANCE, "1 + 2", "1 + 3/5"),
        real(RealField.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)"),
        bigdecimal(BigDecimalField.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)"),
        gaussian(GaussianRationals.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" * 8i"),
        complex(ComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)", "exp(i * pi)", "\"2 + 3i\" * i"),
        bigcomplex(BigComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" * 8i")
        ;

        private final Field<?> field;
        private final String[] examples;

        FieldInformation(Field<?> field, String... examples) {
            this.field = field;
            this.examples = examples;
        }
    }


   // tag::eval[]

    public static String eval(String expression, String field) {

        Field<?> f = FieldInformation.valueOf(field).getField();
        Expression<?> parsedExpression = AST.parseInfix(expression, f);
        try (ConfigurationService.Reset r = ConfigurationService.setConfiguration(cb ->
            cb.configure(UncertaintyConfiguration.class,
                    (ub) -> ub.withNotation(ROUND_VALUE))
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
