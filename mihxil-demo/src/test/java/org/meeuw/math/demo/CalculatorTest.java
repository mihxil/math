package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.util.logging.Level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.demo.Calculator.FieldInformation.*;

@Log
class CalculatorTest {
    static {
        DemoUtils.setupLogging(Level.FINE);
    }

    @Test
    void gaussian() {
        String result = Calculator.eval("""
           "i"  * "1 + 3i"
           """, "gaussian");
        assertThat(result).isEqualTo("-3 + i");
    }

    @Test
    void quaternions() {
        String result = Calculator.eval("-i * i", quaterniongroup.name());
        assertThat(result).isEqualTo("e");
    }

    @Test
    void natural() {
        String result = Calculator.eval("2 ^ 10", natural.name());
        assertThat(result).isEqualTo("1024");
    }

    @Test
    void modulo() {
        String result = Calculator.eval("2 ^ 11", modulo13.name());
        assertThat(result).isEqualTo("7");
    }

    @Test
    void complex() {
        String result = Calculator.eval("1 + 2", complex.name());
        assertThat(result).isEqualTo("3");
    }


    @Test
    void integers() {
        log.fine("test");
        {
            String result = Calculator.eval("11 \\ 3", integers.name());
            assertThat(result).isEqualTo("3");
        }
         {
            String result = Calculator.eval("11 % 3", integers.name());
            assertThat(result).isEqualTo("2");
        }
    }

    @Test
    void polynomials() {
        log.fine("test");
        {
            String result = Calculator.eval("""
            "x + 2x^2 + x^5" * "7 + x"
            """, polynomials.name());
            assertThat(result).isEqualTo("7·x + 15·x² + 2·x³ + 7·x⁵ + x⁶");
        }

    }
}
