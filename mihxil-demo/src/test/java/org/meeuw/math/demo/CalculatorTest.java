package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.util.logging.Level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Log
public class CalculatorTest {
    static {
        DemoUtils.setupLogging(Level.FINE);
    }

    @Test
    public void gaussian() {
        String result = Calculator.eval("""
           "i"  * "1 + 3i"
           """, "gaussian");
        assertThat(result).isEqualTo("-3 + i");
    }

    @Test
    public void quaternions() {
        DemoUtils.setupLogging("FINE");
        String result = Calculator.eval("-i * i", Calculator.FieldInformation.quaterniongroup.name());
        assertThat(result).isEqualTo("e");
    }

    @Test
    public void natural() {
        DemoUtils.setupLogging("FINE");
        String result = Calculator.eval("2 ^ 10", Calculator.FieldInformation.natural.name());
        assertThat(result).isEqualTo("1024");
    }

    @Test
    public void modulo() {
        DemoUtils.setupLogging("FINE");
        String result = Calculator.eval("2 ^ 11", Calculator.FieldInformation.modulo13.name());
        assertThat(result).isEqualTo("7");
    }


    @Test
    public void integers() {
        log.fine("test");
        {
            String result = Calculator.eval("11 \\ 3", Calculator.FieldInformation.integers.name());
            assertThat(result).isEqualTo("3");
        }
         {
            String result = Calculator.eval("11 % 3", Calculator.FieldInformation.integers.name());
            assertThat(result).isEqualTo("2");
        }
    }


    @Test
    public void polynomials() {
        log.fine("test");
        {
            String result = Calculator.eval("""
            "x + 2x^2 + x^5" * "7 + x"
            """, Calculator.FieldInformation.polynomials.name());
            assertThat(result).isEqualTo("7·x + 15·x² + 2·x³ + 7·x⁵ + x⁶");
        }

    }
}
