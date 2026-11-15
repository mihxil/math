package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.uncertainnumbers.Uncertain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.demo.Calculator.FieldInformation.*;

@Log
class CalculatorTest {
    static {
        DemoUtils.setupLogging(Level.INFO);
    }

    @Test
    void gaussian() {
        var calculator = new Calculator<>(gaussian);
        String result = calculator.evalToString("""
           "i"  * "1 + 3i"
           """);
        assertThat(result).isEqualTo("-3 + i");
    }

    @Test
    void quaternions() {
        var calculator = new Calculator<>(quaterniongroup);
        String result = calculator.evalToString("-i * i");
        assertThat(result).isEqualTo("e");
    }

    @Test
    void natural() {
        var calculator = new Calculator<>(natural);
        String result = calculator.evalToString("2 ^ 10");
        assertThat(result).isEqualTo("1024");
    }

    @Test
    void modulo() {
        var calculator = new Calculator<>(modulo13);

        String result = calculator.evalToString("2 ^ 11");
        assertThat(result).isEqualTo("7");
    }

    @Test
    void complex() {
        var calculator = new Calculator<>(complex);

        String result = calculator.evalToString("""
            "2 + 3i" ⋅ i
            """);
        assertThat(result).isEqualTo("-3 + 2i");


        assertThat(calculator.evalToString("4 * 4")).isEqualTo("16");
        assertThat(calculator.calculate("sin(\uD835\uDF0B/2)").result()).isEqualTo("1");
        assertThat(calculator.calculate("exp(-i ⋅ \uD835\uDF0B)").result()).isEqualTo("-1");

    }


    @Test
    void integers() {
        var  calculator = new Calculator<>(integers);

        {
            String result = calculator.evalToString("11 \\ 3");
            assertThat(result).isEqualTo("3");
        }
         {
            String result = calculator.evalToString("11 % 3");
             assertThat(result).isEqualTo("2");
        }
    }

    @Test
    void squares() {
        var calculator = new Calculator<>(squares);
        assertThat(calculator.evalToString("4 * 4")).isEqualTo("16");
        assertThatThrownBy(() -> calculator.eval("2 *  8")).isInstanceOf(InvalidElementCreationException.class);
    }

    @Test
    void integerAndFraction() {
        var calculator = new Calculator<>(rational);
        assertThat(calculator.evalToString("1³⁄₅")).isEqualTo("1³⁄₅");
        assertThat(calculator.evalToString("-1³⁄₅")).isEqualTo("-1³⁄₅");
        assertThat(calculator.evalToString("2 - 1³⁄₅")).isEqualTo("²⁄₅");
        assertThat(calculator.evalToString("13/5")).isEqualTo("2³⁄₅");
    }


    @Test
    void bigdecimal() {
        // The test framework (ConfigurationExtension) applies a test-specific configuration (ROUND_VALUE notation, 50 fraction digits).
        // The browser uses the real defaults, so use those.
        var calculator = new Calculator<>(bigdecimal);
        assertThat(calculator.evalToString("4 * 4")).isEqualTo("16");
        assertThat(calculator.calculate("sin(\uD835\uDF0B/2)").result()).isEqualTo("1");
    }


    /**
     * Typed numbers are exact. (In ROUND_VALUE notation '1' would be parsed as 1 ± 5, and 1/3 would be 0)
     */
    @Test
    void literalsAreExact() {
        assertThat(new Calculator<>(real).evalToString("1/3")).isEqualTo("0.3333333333333333");
        assertThat(new Calculator<>(complex).evalToString("1/3")).startsWith("0.33333333333333");
        assertThat(new Calculator<>(complex).evalToString("sin(𝜋/2)")).isEqualTo("1");
    }

    @Test
    void maximalPrecision() {
        assertThat(new Calculator<>(real).withMaximalPrecision(5).evalToString("1/3")).isEqualTo("0.33333");
        assertThat(new Calculator<>(bigdecimal).withMaximalPrecision(5).evalToString("1/3")).isEqualTo("0.33333");
        assertThat(new Calculator<>(complex).withMaximalPrecision(3).evalToString("𝜋")).isEqualTo("3.142");
        assertThat(new Calculator<>(bigcomplex).withMaximalPrecision(3).evalToString("𝜋")).isEqualTo("3.142");
        // trailing zeros are still stripped
        assertThat(new Calculator<>(bigdecimal).withMaximalPrecision(5).evalToString("1/4")).isEqualTo("0.25");
        assertThatThrownBy(() -> new Calculator<>(real).withMaximalPrecision(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Results are shown rounded, but when used in a next calculation, the full precision is used.
     */
    @Test
    void previousResultHasFullPrecision() {
        var calculator = new Calculator<>(real).withMaximalPrecision(5);
        var third = calculator.calculate("1/3");
        assertThat(third.result()).isEqualTo("0.33333");
        assertThat(calculator.evalToString(third.operand() + " * 3")).isEqualTo("1");
        assertThat(calculator.evalToString("3 * " + third.operand())).isEqualTo("1");

        // negative
        var minusThird = calculator.calculate("-1/3");
        assertThat(minusThird.result()).isEqualTo("-0.33333");
        assertThat(calculator.evalToString(minusThird.operand() + " * 3")).isEqualTo("-1");

        // also after changing the precision
        var moreDigits = calculator.withMaximalPrecision(10);
        assertThat(moreDigits.evalToString(third.operand())).isEqualTo("0.3333333333");
        assertThat(moreDigits.withDefaultMaximalPrecision().evalToString(third.operand())).isEqualTo("0.3333333333333333");

        // if it is edited, it is just the number
        assertThat(calculator.evalToString("0.333331 * 3")).isEqualTo("0.99999");
    }

    @Test
    void previousResultHasFullPrecisionComplex() {
        var calculator = new Calculator<>(complex).withMaximalPrecision(5);
        var result = calculator.calculate("\"1 + 2i\" / 3");
        assertThat(result.result()).isEqualTo("0.33333 + 0.66667i");
        assertThat(result.operand()).isEqualTo("\"0.33333 + 0.66667i\"");
        assertThat(calculator.evalToString(result.operand() + " ⋅ 3")).isEqualTo("1 + 2i");
    }

    @Test
    void settings() {
        assertThat(java.util.Arrays.stream(Calculator.FieldInformation.values())
            .filter(fi -> List.of(fi.settings()).contains("maximalPrecision")))
            .containsExactlyInAnyOrder(real, bigdecimal, complex, bigcomplex, quaternions_bigdecimal);
        assertThat(java.util.Arrays.stream(Calculator.FieldInformation.values())
            .filter(fi -> List.of(fi.settings()).contains("maximalDuration")))
            .containsExactlyInAnyOrder(rational, bigdecimal, gaussian, bigcomplex, quaternions, quaternions_bigdecimal, integers, natural, even, odd, squares, polynomials);
    }

    @Test
    void resultAsOperand() {
        var calculator = new Calculator<>(gaussian);
        var result = calculator.calculate("\"1 + 3i\" ⋅ i");
        assertThat(result.result()).isEqualTo("-3 + i");
        assertThat(result.operand()).isEqualTo("\"-3 + i\"");
        assertThat(calculator.evalToString(result.operand() + " ⋅ i")).isEqualTo("-1 - 3i");
    }

    @Test
    void maximalDuration() {
        var calculator = new Calculator<>(integers).withMaximalDuration(Duration.ofMillis(500));
        long start = System.nanoTime();
        var result = calculator.calculate("(100!)!");
        Duration took = Duration.ofNanos(System.nanoTime() - start);
        log.info(() -> "Took " + took + ": " + result);
        assertThat(result.result()).isNull();
        assertThat(result.error()).isEqualTo("Calculation took longer than 500 ms");
        assertThat(took).isLessThan(Duration.ofSeconds(5));

        // the calculator is still usable
        assertThat(calculator.evalToString("5!")).isEqualTo("120");

        assertThatThrownBy(() -> new Calculator<>(integers).withMaximalDurationInSeconds(1.5).eval("(100!)!!"))
            .isInstanceOf(CancellationException.class)
            .hasMessage("Calculation took longer than 1.5 s");
        assertThat(new Calculator<>(integers).getMaximalDuration()).isEqualTo(Calculator.DEFAULT_MAXIMAL_DURATION);
        assertThat(calculator.withDefaultMaximalDuration().getMaximalDuration()).isEqualTo(Calculator.DEFAULT_MAXIMAL_DURATION);
        assertThat(new Calculator<>(integers).withMaximalDuration(null).evalToString("5!")).isEqualTo("120");
        assertThatThrownBy(() -> new Calculator<>(integers).withMaximalDuration(Duration.ZERO)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void calculateError() {
        var calculator = new Calculator<>(integers);
        var result = calculator.calculate("1 +");
        assertThat(result.result()).isNull();
        assertThat(result.error()).isNotBlank();
    }

    @Test
    void unaryOperatorSyntax() {
        assertThat(new Calculator<>(integers).evalToString("-(1 + 2)")).isEqualTo("-3");
        assertThat(new Calculator<>(integers).evalToString("2 ⋅ -(1 + 2)")).isEqualTo("-6");
        assertThat(new Calculator<>(integers).evalToString("5!")).isEqualTo("120");
        assertThat(new Calculator<>(integers).evalToString("5!!")).isEqualTo("15");
        assertThat(new Calculator<>(integers).evalToString("!4")).isEqualTo("9");
        assertThat(new Calculator<>(polynomials).evalToString("\"x^3\"'")).isEqualTo("3·x²");
        assertThat(new Calculator<>(real).evalToString("γ")).startsWith("0.577");
        assertThat(new Calculator<>(complex).calculate("1 eml 1").error()).isNull();
    }

    /**
     * Every key of the keypad must produce something that can be parsed.
     */
    @ParameterizedTest
    @EnumSource(Calculator.FieldInformation.class)
    void keys(Calculator.FieldInformation fi) {
        var calculator = new Calculator<>(fi);
        var keys = List.of(fi.keys());
        String sample = null;
        for (var key : keys) {
            if (key.type().equals("element")) {
                if (key.insert().matches("[0-9.]")) {
                    // digits are for typing numbers, they need not be valid on their own (e.g. 2 is not a square)
                    continue;
                }
                var result = calculator.calculate(key.insert());
                assertThat(result.error()).withFailMessage(() -> key + ": " + result.error()).isNull();
                if (sample == null) {
                    sample = key.insert();
                }
            }
        }
        if (sample == null || keys.stream().anyMatch(k -> k.insert().equals("2"))) {
            sample = calculator.calculate("2").error() == null ? "2" : calculator.calculate("4").error() == null ? "4" : sample;
        }
        assertThat(sample).isNotNull();
        for (var key : keys) {
            if (key.type().equals("unary")) {
                String input = key.insert().replace(Calculator.SLOT, sample);
                var result = calculator.calculate(input);
                // the operation itself may fail (e.g. no square root of 2 in the integers), but it must be parseable
                assertThat(result.error() == null ? "" : result.error())
                    .withFailMessage(() -> input + ": " + result.error())
                    .doesNotContain("Encountered", "Lexical", "No such");
            } else if (key.type().equals("binary")) {
                String input = sample + " " + key.insert() + " " + sample;
                var result = calculator.calculate(input);
                assertThat(result.error() == null ? "" : result.error())
                    .withFailMessage(() -> input + ": " + result.error())
                    .doesNotContain("Encountered", "Lexical", "No such");
            }
        }
    }

    @Test
    void polynomials() {
        log.fine("test");
        var calculator = new Calculator<>(polynomials);

        {
            String result = calculator.evalToString("""
            "x + 2x^2 + x^5" * "7 + x"
            """);
            assertThat(result).isEqualTo("7·x + 15·x² + 2·x³ + 7·x⁵ + x⁶");
        }
    }

    @ParameterizedTest
    @EnumSource(Calculator.FieldInformation.class)
    void tryAll(Calculator.FieldInformation fi) {
        var calculator = new Calculator<>(fi);
        log.info(calculator.toString());
        for (String e : fi.examples()) {
            var evaluated = calculator.evalToString(e);
            log.info(e + " = " + evaluated);
            var evalEvaluated = calculator.evalToString(evaluated);
            assertThat(evalEvaluated)
                .withFailMessage(() -> "eval(" + evaluated + ") = " + evalEvaluated + " (should be " + evaluated + ")")
                .isEqualTo(evaluated);

            // the result can be used as an operand of a next calculation
            var calculated = calculator.calculate(e);
            assertThat(calculated.result()).isEqualTo(evaluated);
            var evalOperand = calculator.evalToString(calculated.operand());
            assertThat(evalOperand)
                .withFailMessage(() -> "eval(" + calculated.operand() + ") = " + evalOperand + " (should be " + evaluated + ")")
                .isEqualTo(evaluated);
            boolean exact = !(calculator.eval(e) instanceof Uncertain uncertain) || uncertain.isExact();
            if (exact && fi.binaryOperators().length > 0) {
                // combined with an operator too, and then it must be the same as the original expression between brackets
                // (only for exact results, a displayed uncertain value is rounded, so continuing with it may differ within the uncertainty)
                String op = fi.binaryOperators()[0];
                String viaOperand = calculator.evalToString(calculated.operand() + " " + op + " " + calculated.operand());
                String viaBrackets = calculator.evalToString("(" + e + ") " + op + " (" + e + ")");
                assertThat(viaOperand)
                    .withFailMessage(() -> calculated.operand() + " " + op + " " + calculated.operand() + " = " + viaOperand + " (should be " + viaBrackets + ")")
                    .isEqualTo(viaBrackets);
            }
        }
    }
}
