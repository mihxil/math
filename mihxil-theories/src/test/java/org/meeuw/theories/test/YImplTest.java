package org.meeuw.theories.test;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class YImplTest {

    @Test
    public void test() {
        assertThat(new YImpl().first()).isEqualTo("a");
    }
}
