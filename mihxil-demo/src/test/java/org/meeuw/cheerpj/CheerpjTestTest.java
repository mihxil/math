package org.meeuw.cheerpj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CheerpjTestTest {

    @Test
    public void test() {
        assertThat(CheerpjTest.getSupportedOperators()).isEqualTo("[OPERATION]");
    }

}
