package org.meeuw.math.test;

import lombok.Getter;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.logging.Level;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.DivisionRing;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.quaternions.Quaternions;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.arithmetic.ast.AST;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;

@Log
public class Calculator {
    static {
        Application.setupLogging();
    }


    @Getter
    public  enum FieldInformation {
        rational(RationalNumbers.INSTANCE, "1 + 2", "1 + 3/5"),
        real(RealField.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)"),
        bigdecimal(BigDecimalField.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)"),
        gaussian(GaussianRationals.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" * 8i"),
        complex(ComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "sin(pi/2)", "exp(i * pi)", "\"2 + 3i\" * i"),
        bigcomplex(BigComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" * 8i"),
        quaternions(Quaternions.of(RationalNumbers.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" * 8i")
        ;

        private final DivisionRing<?> field;
        private final String[] examples;

        FieldInformation(DivisionRing<?> field, String... examples) {
            this.field = field;
            this.examples = examples;
        }
    }


   // tag::eval[]

    public static String eval(String expression, String field) {

        DivisionRing<?> f = FieldInformation.valueOf(field).getField();
        log.info("Evaluating expression: " + expression);
        Expression<?> parsedExpression = AST.parseInfix(expression, f);
        try (ConfigurationService.Reset r = ConfigurationService.setConfiguration(cb ->
            cb.configure(UncertaintyConfiguration.class,
                    (ub) -> ub.withNotation(ROUND_VALUE))
                .configure(MathContextConfiguration.class,
                    (mc) ->
                        mc.withContext(new MathContext(Utils.PI.length())))
        )) {
            try {
                AlgebraicElement<?> result = parsedExpression.eval();
                log.info("Result " + result);
                return result.toString();
            } catch (Exception ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
                throw ex;
            }
        }
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        System.out.println(arg + " = " + eval(arg, f));
    }
}
