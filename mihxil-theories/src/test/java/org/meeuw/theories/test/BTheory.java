package org.meeuw.theories.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

public interface BTheory<T extends B> extends ATheory<T> {


    @Property
    default void jAlsoNotNegative(@ForAll("datapoints") T b) {
        assertThat(b.getJ()).isNotNegative();
    }
}
