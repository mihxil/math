package org.meeuw.math.demo;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.*;
import java.util.logging.Level;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
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
@ToString
public class Calculator<E extends MagmaElement<E>, F extends Magma<E>> {

    public Calculator(FieldInformation fi) {
        this((F) fi.getField());
    }

    public Calculator(F field) {
        this.field = field;
    }

    // tag::eval[]

    @Getter
    public enum FieldInformation {
        rational(RationalNumbers.INSTANCE,
            "1 + 2",
            "1 + 3/5"
        ),
        real(RealField.INSTANCE,
            "1 + 2",
            "1 + 3/5",
            "sin(𝜋/2)",
            "sqr(𝜑) - 𝜑"
        ),
        bigdecimal(BigDecimalField.INSTANCE,
            "1 + 2",
            "1 + 3/5",
            "sin(𝜋/2)"
        ),
        gaussian(GaussianRationals.INSTANCE,
            "1 + 2",
            "1 + 3/5",
            "\"1 + 2i\" ⋅ 8i"
        ),
        complex(ComplexNumbers.INSTANCE,
            "1 + 2",
            "1 + 3/5",
            "sin(𝜋/2)",
            "exp(-i ⋅ 𝜋)",
            "\"2 + 3i\" ⋅ i"
        ),
        bigcomplex(BigComplexNumbers.INSTANCE,
            "1 + 2",
            "1 + 3/5",
            "\"1 + 2i\" ⋅ 8i"
        ),
        quaternions(Quaternions.of(RationalNumbers.INSTANCE),
            "1 + 2",
            "1 + 3/5",
            "\"1 + 2i + 3j + 4k\" ⋅ 8i"
        ),
        quaternions_bigdecimal(Quaternions.of(BigDecimalField.INSTANCE),
            "1 + 2",
            "1 + 3/5",
            "\"1 + 2i + 3j + 4k\" ⋅ 8i"
        ),
        integers(Integers.INSTANCE,
            "4 ⋅ 7",
            "9 - 3",
            "69!"
        ),
        modulo10(ModuloRing.of(10),
            "4 ⋅ 7",
            "9 - 3"
        ),
        modulo13(ModuloField.of(13),
            "10 ⋅ 7",
            "10 - 3",
            "12 ⋅ 6 / 4"
        ),
        natural(NaturalNumbers.INSTANCE,
            "10 ⋅ 7",
            "10 + 3",
            "12 ⋅ 6 + 0"
        ),
        even(EvenIntegers.INSTANCE,
            "10 ⋅ 8",
            "10 - 4"
        ),
        odd(OddIntegers.INSTANCE,
            "1 ⋅ 9",
            "13 ⋅ 5"
        ),
        squares(Squares.INSTANCE,
            "4 ⋅ 9"
        ),
        klein(KleinGroup.INSTANCE,
            "a * b * c * e",
            "a * b"
        ),
        quaterniongroup(QuaternionGroup.INSTANCE,
            "i * j",
            "j * i",
            "e * k * i"
        ),
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
        );

        private final Magma<?> field;
        private final String[] examples;

        private final String[] binaryOperators;
        private final String[] unaryOperators;

        private final boolean finite;

        FieldInformation(Magma<?> field, String... examples) {
            this.field = field;
            this.finite = field.isFinite();
            this.examples = examples;

            this.binaryOperators = field.getSupportedOperators()
                .stream()
                .map(AlgebraicBinaryOperator::getSymbol)
                .toArray(String[]::new);

            this.unaryOperators = field.getSupportedUnaryOperators()
                .stream()
                .map(AlgebraicUnaryOperator::getSymbol)
                .toArray(String[]::new);

            log.fine(() -> "Created %s, operators: %s, unary: %s examples: %s, elements: %s".formatted(field,
                List.of(binaryOperators),
                List.of(unaryOperators),
                List.of(examples), List.of(getElements())));
        }

        public String[] getElements() {
            return elements(this.field);
        }

        public static String[] elements(Magma<?> field) {
            Set<String> elements = new LinkedHashSet<>(field.getConstants().keySet());
            if (field.getCardinality().isCountable() && field instanceof Streamable<?> streamable) {
                try {
                    streamable.stream().limit(100).map(Object::toString).forEach(elements::add);
                } catch (NotStreamable ignored) {
                }
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

    private final F field;


    public E eval(String input) {
        final String expression = input.strip();
        try (var r = ConfigurationService.setConfiguration(cb -> cb
            .configure(UncertaintyConfiguration.class,
                (ub) -> ub.withNotation(ROUND_VALUE))
            .configure(MathContextConfiguration.class,
                (mc) -> mc.withContext(new MathContext(Utils.PI.length())))
        )) {

            log.fine(() -> "Evaluating expression in %s: %s. Binary: %s, Unary: %s".formatted(field, expression, field.getSupportedOperators(), field.getSupportedUnaryOperators()));
            if (field.getSupportedOperators().isEmpty()) {
                log.log(Level.SEVERE,  "Supported operators is empty for " + field);
            }
            var parsedExpression = AST.parse(expression, field);
            log.fine(() -> "Parsed expression: %s".formatted(parsedExpression));
            return parsedExpression.eval();
        } catch (Throwable ex) {
            log.log(Level.SEVERE, ex.getClass() + " " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.finer("Ready evaluation");
        }
    }

    public String evalToString(String input) {
        var result = eval(input);
        var resultAsString = result.toString();
        log.info(() -> "Result: %s = %s".formatted(input.strip(), resultAsString));
        return resultAsString;
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        var calculator = new Calculator<>(FieldInformation.valueOf(f));
        System.out.println(arg + " = " + calculator.eval(arg));
    }
}
