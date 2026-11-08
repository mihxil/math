package org.meeuw.math.demo;

import lombok.*;
import lombok.extern.java.Log;

import java.math.MathContext;
import java.util.*;
import java.util.logging.Level;

import org.meeuw.configuration.Configuration;
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
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.ROUND_VALUE;

@Log
@ToString
public class Calculator<E extends MagmaElement<E>, F extends Magma<E>> {

    public Calculator(FieldInformation fi) {
        this((F) fi.field(), ConfigurationService.getConfiguration());
    }

    public Calculator(F field) {
        this(field, ConfigurationService.getConfiguration());
    }

    @lombok.Builder
    private Calculator(F field, Configuration configuration) {
        this.field = field;
         this.configuration = configuration == null ? Configuration.builder()
            .configure(UncertaintyConfiguration.class,
                (ub) -> ub.withNotation(ROUND_VALUE))
            .configure(MathContextConfiguration.class,
                (mc) -> mc.withContext(new MathContext(Utils.PI.length())))
            .build() : configuration;
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
                List.of(examples), List.of(elements())));
        }

        public String[] elements() {
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

        /**
         * The keys of an on-screen keypad for this structure.
         * <ul>
         *   <li>{@code element}: for finite structures all elements, otherwise digits, constants, imaginary units, the variable of polynomials,
         *   as far as the structure accepts them</li>
         *   <li>{@code binary}: the binary operators</li>
         *   <li>{@code unary}: the unary operators. {@link Key#insert()} is then a template, where {@link #SLOT} is to be replaced with the operand</li>
         * </ul>
         */
        public Key[] keys() {
            if (keys == null) {
                List<Key> result = new ArrayList<>();
                elementKeys(result);
                boolean hasMultiplication = field.getSupportedOperators().stream().anyMatch(op -> op.getSymbol().equals("⋅"));
                for (AlgebraicBinaryOperator op : field.getSupportedOperators()) {
                    if (hasMultiplication && op.getSymbol().equals("*")) {
                        // '*' is the generic group operation, which here is the same as '⋅'
                        continue;
                    }
                    result.add(new Key(op.getSymbol(), op.getSymbol(), "binary", title(op.name())));
                }
                for (AlgebraicUnaryOperator op : field.getSupportedUnaryOperators()) {
                    String template = inputTemplate(op);
                    if (template != null) {
                        result.add(new Key(op.stringify(SLOT), template, "unary", title(op.name())));
                    }
                }
                keys = result.toArray(new Key[0]);
            }
            return keys;
        }
        private Key[] keys;

        private void elementKeys(List<Key> result) {
            if (finite && field instanceof Streamable<?> streamable) {
                streamable.stream().limit(100).map(Object::toString).forEach(e ->
                    result.add(new Key(e, AST.asOperand(e), "element", null))
                );
                return;
            }
            for (int i = 0; i <= 9; i++) {
                result.add(new Key(String.valueOf(i), String.valueOf(i), "element", null));
            }
            if (accepts("1.5")) {
                result.add(new Key(".", ".", "element", "decimal point"));
            }
            Set<String> candidates = new LinkedHashSet<>();
            for (String constant : field.getConstants().keySet()) {
                // names like 'unity', 'zero', 'pi' are aliases, and not useful as keys
                if (!constant.matches("[a-z]{2,}")) {
                    candidates.add(constant);
                }
            }
            candidates.addAll(List.of("i", "j", "k"));
            for (String candidate : candidates) {
                if (accepts(candidate)) {
                    result.add(new Key(candidate, candidate, "element", null));
                }
            }
            if (field instanceof PolynomialRing<?> polynomialRing) {
                result.add(new Key(polynomialRing.getVariable(), "\"" + polynomialRing.getVariable() + "\"", "element", "the variable"));
            }
        }

        /**
         * Whether the given input is a valid expression for this structure (without logging, failure is normal here)
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private boolean accepts(String input) {
            try {
                AST.parse(input, (AlgebraicStructure) field).eval();
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }

        private static String title(String name) {
            return name.toLowerCase().replace('_', ' ');
        }

        public String description() {
            return field.getClass().getSimpleName() + " " + field;
        }

        public String help() {
            return field.getDescription().orElse(null);
        }
    }

    /**
     * In templates for unary operators, the place where the operand goes.
     */
    public static final String SLOT = "□";

    /**
     * A key on the on-screen keypad.
     * @param label  what is shown on the key
     * @param insert what is inserted in the expression (for unary operators a template containing {@link #SLOT})
     * @param type   'element', 'binary' or 'unary'
     * @param title  a description, or {@code null}
     */
    public record Key(String label, String insert, String type, String title) {
    }

    /**
     * How a unary operator can be entered in the infix notation, with {@link #SLOT} for the operand. Or {@code null} if it can't (or there is no point, like for the identity '+').
     */
    static String inputTemplate(AlgebraicUnaryOperator op) {
        String symbol = op.getSymbol();
        String x = TextUtils.PLACEHOLDER;
        if (symbol.matches("[a-z][a-z0-9]*")) {
            // function notation, like sqr(x), sin(x), inverse(x)
            return symbol + "(" + SLOT + ")";
        }
        if (symbol.equals("-")) {
            return "-(" + SLOT + ")";
        }
        if (symbol.equals("'")) {
            return SLOT + "'";
        }
        if (symbol.length() > 1 && symbol.startsWith(x)) {
            // postfix, like x!
            return SLOT + symbol.substring(x.length());
        }
        if (symbol.length() > 1 && symbol.endsWith(x)) {
            // prefix, like !x
            return symbol.substring(0, symbol.length() - x.length()) + SLOT;
        }
        return null;
    }

    @With
    private final F field;

    @With
    private final Configuration configuration;


    public E eval(String input) {
        final String expression = input.strip();
        try (var r = ConfigurationService.setConfiguration(configuration)) {
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
        log.fine(() -> "Result: %s = %s".formatted(input.strip(), resultAsString));
        return resultAsString;
    }

    /**
     * Evaluates, but never throws. Meant for the web app.
     * @param result  the result as a string, or {@code null} if evaluation failed
     * @param operand the result in a form that can be used as an operand in a next expression (see {@link AST#asOperand(String)}), or {@code null}
     * @param error   a message describing why evaluation failed, or {@code null}
     */
    public record Result(String input, String result, String operand, String error) {
    }

    public Result calculate(String input) {
        try {
            String result = evalToString(input);
            return new Result(input, result, AST.asOperand(result), null);
        } catch (RuntimeException e) {
            String message = e.getMessage() == null || e.getMessage().isBlank() ? e.getClass().getSimpleName() : e.getMessage().strip();
            return new Result(input, null, null, message);
        }
    }
    //end::eval[]

    public static void main(String[] argv) {
        String arg = argv[0];
        String f = argv.length > 1 ? argv[1] : "rational";
        var calculator = new Calculator<>(FieldInformation.valueOf(f))
            .withConfiguration(ConfigurationService.getConfiguration());
        System.out.println(arg + " = " + calculator.eval(arg));
    }
}
