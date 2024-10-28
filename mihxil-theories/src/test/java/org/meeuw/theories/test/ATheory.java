package org.meeuw.theories.test;


import net.jqwik.api.*;
import org.assertj.core.api.Assertions;

import org.checkerframework.checker.nullness.qual.Nullable;


public interface ATheory<T extends A>  {


    @Property
    default void toStringNoErrors(@ForAll("datapointsOrNull") Object object) {// Must be Object!
        Assertions.assertThatNoException().isThrownBy(() -> {
            System.out.println("" + object);
        });
   }

    @Provide
    Arbitrary<T> datapoints();


     @Provide
    default Arbitrary<@Nullable T> datapointsOrNull() {
        return datapoints()
            .injectNull(0.1);
    }




}
