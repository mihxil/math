package org.meeuw.math.demo;

import lombok.Getter;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.*;
import java.util.logging.Level;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Magma;
import org.meeuw.math.abstractalgebra.Streamable;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.abstractalgebra.integers.*;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.polynomial.PolynomialRing;
import org.meeuw.math.abstractalgebra.quaternions.Quaternions;
import org.meeuw.math.abstractalgebra.quaternions.q8.QuaternionGroup;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.arithmetic.ast.AST;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;

@Log
public class Calculator {

    // tag::eval[]

    @Getter
    public  enum FieldInformation {
        rational(RationalNumbers.INSTANCE, "1 + 2", "1 + 3/5"),
        real(RealField.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)", "sqr(𝜑) - 𝜑"),
        bigdecimal(BigDecimalField.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)"),
        gaussian(GaussianRationals.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" ⋅ 8i"),
        complex(ComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)", "exp(-i ⋅ 𝜋)", "\"2 + 3i\" ⋅ i"),
        bigcomplex(BigComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" ⋅ 8i"),
        quaternions(Quaternions.of(RationalNumbers.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" ⋅ 8i"),
        quaternions_bigdecimal(Quaternions.of(BigDecimalField.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" ⋅ 8i"),
        integers(Integers.INSTANCE, "4 ⋅ 7", "9 - 3"),
        modulo10(ModuloRing.of(10), "4 ⋅ 7", "9 - 3"),
        modulo13(ModuloField.of(13), "10 ⋅ 7", "10 - 3", "12 ⋅ 6 / 4"),
        natural(NaturalNumbers.INSTANCE, "10 ⋅ 7", "10 - 3", "12 ⋅ 6 / 4"),
        even(EvenIntegers.INSTANCE, "10 ⋅ 8", "10 - 4"),
        squares(Squares.INSTANCE, "2 ⋅ 9"),

        klein(KleinGroup.INSTANCE,
            "a * b * c * e",
            "a * b"
        ),
        quaterniongroup(QuaternionGroup.INSTANCE, "i", "e" ),
        dihedral3(DihedralGroup.D3,
            "r1 * r2",
            "s0 * r1 * s0"
        ),
        dihedral4(DihedralGroup.of(4),
            "r1 * r2",
            "s0 * r1 * s0 * s3"
        ),
        polynomials(PolynomialRing.of(GaussianRationals.INSTANCE),
            "\"7·x + 15·x² + 2·x³ + 7·x⁵ + x⁶\" ⋅ \"15·x² + 2·x³\"",
            "\"x + 2x^2 + x^5\" + \"5 + 3/4x^2 - x^5\""
        )
        ;

        private final Magma<?> field;
        private final String[] examples;
        private final String[] elements;
        private final String[] binaryOperators;
        private final String[] unaryOperators;

        private final boolean finite;

        FieldInformation(Magma<?> field, String... examples) {
            this.field = field;
            this.finite = field.isFinite();
            this.examples = examples;
            this.elements = elements(field);
            this.binaryOperators = field.getSupportedOperators()
                .stream()
                .map(AlgebraicBinaryOperator::getSymbol)
                .toArray(String[]::new);

            this.unaryOperators = field.getSupportedUnaryOperators()
                .stream()
                .map(AlgebraicUnaryOperator::getSymbol)
                .toArray(String[]::new);

            log.fine("Created %s, operators: %s, unary: %s examples: %s, elements: %s".formatted(field,
                List.of(binaryOperators),
                List.of(unaryOperators),
                List.of(examples), List.of(elements)));
        }

        public static String[] elements(Magma<?> field) {
            Set<String> elements = new LinkedHashSet<>(field.getConstants().keySet());
            if (field.getCardinality().isCountable() && field instanceof  Streamable<?> streamable) {
                try {
                    streamable.stream().limit(100).map(Object::toString).forEach(elements::add);
                } catch (NotStreamable ignored) {}
            }
            return elements.toArray(new String[0]);
        }

        public String getDescription() {
            return field.getClass().getSimpleName() + " " + field;
        }

        public String getHelp() {
            return field.getDescription().orElse(null);
        }
    }



    public static String eval(String input, final String field) {
        final String expression = input.strip();
        try (var r = ConfigurationService.setConfiguration(cb -> cb
            .configure(UncertaintyConfiguration.class,
                (ub) -> ub.withNotation(ROUND_VALUE))
            .configure(MathContextConfiguration.class,
                (mc) -> mc.withContext(new MathContext(Utils.PI.length())))
        )) {
            var f = FieldInformation.valueOf(field).getField();

            log.fine(() -> "Evaluating expression in %s: %s. Binary: %s, Unary: %s".formatted(f, expression, f.getSupportedOperators(), f.getSupportedUnaryOperators()));
            if (f.getSupportedOperators().isEmpty()) {
                log.log(Level.SEVERE,  "Supported operators is empty for " + f);
            }
            var parsedExpression = AST.parse(expression, f);
            log.fine(() -> "Parsed expression: %s".formatted( parsedExpression));
            var result = parsedExpression.eval();
            var resultAsString = result.toString();
            log.info(() -> "Result: %s = %s".formatted(expression, resultAsString));
            return resultAsString;
        } catch (Throwable ex) {
            log.log(Level.SEVERE,  ex.getClass() + " " + ex.getMessage(), ex);
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
