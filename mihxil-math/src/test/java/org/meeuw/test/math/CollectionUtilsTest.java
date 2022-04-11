package org.meeuw.test.math;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import org.meeuw.math.CollectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilsTest {

    @Test
    public void memoize() {
        AtomicInteger value = new AtomicInteger(0);

        Supplier<Integer> i = CollectionUtils.memoize(value::get);
        assertThat(i.get()).isEqualTo(0);
        assertThat(i.get()).isEqualTo(0);
        value.set(5);
        assertThat(i.get()).isEqualTo(0);
    }
}
