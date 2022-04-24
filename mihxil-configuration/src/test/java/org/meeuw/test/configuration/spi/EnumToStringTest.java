package org.meeuw.test.configuration.spi;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.spi.EnumToString;
import org.meeuw.test.configuration.A;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumToStringTest {

    @Test
    public void invalid() {
        assertThat(new EnumToString().fromString(A.class, "z")).isEmpty();
    }
}
