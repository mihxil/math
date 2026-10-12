package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.meeuw.math.exceptions.InvalidElementCreationException;

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
        var calculator = new Calculator<>(complex.getField());

        String result = calculator.evalToString("""
            "2 + 3i" ⋅ i
            """);
        assertThat(result).isEqualTo("-3 + 2i");
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
        for (String e : fi.getExamples()) {
            log.info(e + " - " + calculator.evalToString(e));
        }
    }
}
