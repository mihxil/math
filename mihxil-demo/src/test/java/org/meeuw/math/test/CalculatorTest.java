package org.meeuw.math.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    public void gaussian() {
        String result = Calculator.eval("""
            "i"  * "1 + 3i"
            """, "gaussian");
        assertThat(result).isEqualTo("-3 + i");
    }
}
