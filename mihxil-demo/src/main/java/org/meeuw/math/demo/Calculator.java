package org.meeuw.math.demo;

import lombok.Getter;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.logging.Level;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloRing;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
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
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" * 8i"),
        quaternions_bigdecimal(Quaternions.of(BigDecimalField.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" * 8i"),
        modulo10(ModuloRing.of(10), "4 * 7", "9 - 3"),
        modulo13(ModuloField.of(13), "10 * 7", "10 - 3", "12 * 6 / 4"),
        klein(KleinGroup.INSTANCE,
            "a * b * c * e",
            "a * b"
        )
        ;

        private final Magma<?> field;
        private final String[] examples;

        FieldInformation(Magma<?> field, String... examples) {
            this.field = field;
            this.examples = examples;
        }

        public String getDescription() {
            return field.getDescription();
        }
    }


   // tag::eval[]

    public static String eval(final String expression, final String field) {


        try (ConfigurationService.Reset r = ConfigurationService.setConfiguration(cb ->
            cb.configure(UncertaintyConfiguration.class,
                    (ub) -> ub.withNotation(ROUND_VALUE))
                .configure(MathContextConfiguration.class,
                    (mc) ->
                        mc.withContext(new MathContext(Utils.PI.length())))
        )) {
            Magma<?> f = FieldInformation.valueOf(field).getField();
            log.info("Evaluating expression in %s: %s. Binary: %s, Unary: %s".formatted(f, expression, f.getSupportedOperators(), f.getSupportedUnaryOperators()));
            log.info("Parsing expression: %s".formatted( expression));

            Expression<?> parsedExpression = AST.parseInfix(expression, f);
            try {
                log.info("Parsed expression: %s".formatted( parsedExpression));
                AlgebraicElement<?> result = parsedExpression.eval();
                String resultAsString = result.toString();
                log.info("Result: %s = %s".formatted(expression, resultAsString));
                return resultAsString;
            } catch (Throwable ex) {
                log.log(Level.SEVERE, "odd1:" +  ex.getClass() + " " + ex.getMessage(), ex);
                throw ex;
            }
        } catch (Throwable ex) {
            log.log(Level.SEVERE, "odd: " + ex.getClass() + " " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.finer("Ready evaluation");
        }
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        System.out.println(arg + " = " + eval(arg, f));
    }
}
