package org.meeuw.theories.test;

import net.jqwik.api.*;
import org.assertj.core.api.Assertions;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ObjectTheory<O> {


    @Property
    default void toStringNoErrors(@ForAll("datapointsOrNull") Object object) {
        Assertions.assertThatNoException().isThrownBy(() -> {
            System.out.println("" + object);
        });
    }

    @Provide("datapoints")
    Arbitrary<O> datapoints();

    @Provide
    default Arbitrary<@Nullable O> datapointsOrNull() {
        return datapoints()
            .injectNull(0.1);
    }

}
