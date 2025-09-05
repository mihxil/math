package org.meeuw.math.demo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    @Test
    public void gaussian() {
        String result = Calculator.eval("""
            "i"  * "1 + 3i"
            """, "gaussian");
        assertThat(result).isEqualTo("-3 + i");
    }

    @Test
    public void quaternions() {
        Utils.setupLogging("FINE");
        String result = Calculator.eval("-i * i", Calculator.FieldInformation.quaterniongroup.name());
        assertThat(result).isEqualTo("e");
    }

    @Test
    public void natural() {
        Utils.setupLogging("FINE");
        String result = Calculator.eval("2 ^ 10", Calculator.FieldInformation.natural.name());
        assertThat(result).isEqualTo("1024");
    }
}
