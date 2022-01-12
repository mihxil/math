package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class ReflectionUtilsTest {

    public static class A {
        private static final A a = new A();
        public static final A b = new A();
        public static final String c = "c";

    }

    @Test
    public void forConstants() {
        List<A> result = new ArrayList<>();
        ReflectionUtils.forConstants(A.class, result::add);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(A.b);
    }

}
