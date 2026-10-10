package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.demo.Calculator.FieldInformation.*;

@Log
class CalculatorTest {
    static {
        DemoUtils.setupLogging(Level.INFO);
    }

    @Test
    void gaussian() {
        Calculator calculator = new Calculator(gaussian);
        String result = calculator.eval("""
           "i"  * "1 + 3i"
           """);
        assertThat(result).isEqualTo("-3 + i");
    }

    @Test
    void quaternions() {
        Calculator calculator = new Calculator(quaterniongroup);
        String result = calculator.eval("-i * i");
        assertThat(result).isEqualTo("e");
    }

    @Test
    void natural() {
        Calculator calculator = new Calculator(natural);
        String result = calculator.eval("2 ^ 10");
        assertThat(result).isEqualTo("1024");
    }

    @Test
    void modulo() {
        Calculator calculator = new Calculator(modulo13);

        String result = calculator.eval("2 ^ 11");
        assertThat(result).isEqualTo("7");
    }

    @Test
    void complex() {
        Calculator calculator = new Calculator(complex);

        String result = calculator.eval("""
            "2 + 3i" ⋅ i
            """);
        assertThat(result).isEqualTo("-3 + 2i");
    }


    @Test
    void integers() {
        log.fine("test");
        Calculator calculator = new Calculator(integers);

        {
            String result = calculator.eval("11 \\ 3");
            assertThat(result).isEqualTo("3");
        }
         {
            String result = calculator.eval("11 % 3");
            assertThat(result).isEqualTo("2");
        }
    }

    @Test
    void polynomials() {
        log.fine("test");
        Calculator calculator = new Calculator(polynomials);

        {
            String result = calculator.eval("""
            "x + 2x^2 + x^5" * "7 + x"
            """);
            assertThat(result).isEqualTo("7·x + 15·x² + 2·x³ + 7·x⁵ + x⁶");
        }
    }

    @ParameterizedTest
    @EnumSource(Calculator.FieldInformation.class)
    void tryAll(Calculator.FieldInformation fi) {
        Calculator calculator = new Calculator(fi);
        log.info(calculator.toString());
        for (String e : fi.getExamples()) {
            log.info(e + " - " + calculator.eval(e));
        }
    }
}
