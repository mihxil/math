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
        String result = Calculator.eval("-i * i", Calculator.FieldInformation.quaterniongroup.name());
        assertThat(result).isEqualTo("e");
    }
}
