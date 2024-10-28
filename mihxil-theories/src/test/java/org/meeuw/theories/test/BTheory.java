package org.meeuw.theories.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public interface BTheory<T extends B> extends ATheory<T> {


    @Property
    default void jNotNegative(@ForAll("datapoints") T b) {
        assertThat(b.getJ()).isNotNegative();
    }

    @Property
    default void plusTest(@ForAll("datapoints") T b1, @ForAll("datapointsOrNull") T o2) {
        T b2 = (T) o2; // ugly

        System.out.println("" + b1 + " " + b2);
        assertThatNoException().isThrownBy(() -> {
            b1.plus(b2);
        });


    }
}
