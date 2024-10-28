package org.meeuw.theories.test;


import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;


public interface ATheory<T extends A> {


    @Property
    default void iNotNegative(@ForAll("datapoints") T a) {
        assertThat(a.getI()).isNotNegative();
    }


    @Provide
    Arbitrary<T> datapoints();
    // Arbitrary<T> datapoints(); // issues!
}
