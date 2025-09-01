package org.meeuw.math.demo;

import lombok.Getter;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.logging.Level;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Magma;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.complex.*;
import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloRing;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.quaternions.Quaternions;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.arithmetic.ast.AST;
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
        real(RealField.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)"),
        bigdecimal(BigDecimalField.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)"),
        gaussian(GaussianRationals.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" ⋅ 8i"),
        complex(ComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "sin(𝜋/2)", "exp(i ⋅ 𝜋)", "\"2 + 3i\" ⋅ i"),
        bigcomplex(BigComplexNumbers.INSTANCE, "1 + 2", "1 + 3/5", "\"1 + 2i\" ⋅ 8i"),
        quaternions(Quaternions.of(RationalNumbers.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" ⋅ 8i"),
        quaternions_bigdecimal(Quaternions.of(BigDecimalField.INSTANCE),
            "1 + 2", "1 + 3/5", "\"1 + 2i + 3j + 4k\" ⋅ 8i"),
        modulo10(ModuloRing.of(10), "4 ⋅ 7", "9 - 3"),
        modulo13(ModuloField.of(13), "10 ⋅ 7", "10 - 3", "12 ⋅ 6 / 4"),
        klein(KleinGroup.INSTANCE,
            "a * b * c * e",
            "a * b"
        ),
        dihedral(DihedralGroup.D3,
            "r1 * r3",
            "s0 * r1 * s0"
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
        try (var r = ConfigurationService.setConfiguration(cb -> cb
            .configure(UncertaintyConfiguration.class,
                (ub) -> ub.withNotation(ROUND_VALUE))
            .configure(MathContextConfiguration.class,
                (mc) -> mc.withContext(new MathContext(Utils.PI.length())))
        )) {
            var f = FieldInformation.valueOf(field).getField();
            log.info(() -> "Evaluating expression in %s: %s. Binary: %s, Unary: %s".formatted(f, expression, f.getSupportedOperators(), f.getSupportedUnaryOperators()));
            var parsedExpression = AST.parse(expression, f);
            log.info(() -> "Parsed expression: %s".formatted( parsedExpression));
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
