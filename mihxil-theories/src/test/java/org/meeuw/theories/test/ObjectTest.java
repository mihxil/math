package org.meeuw.theories.test;

import net.jqwik.api.*;

public interface ObjectTest<T> {


    @Property
    default void hashCode(@ForAll("datapoints") Object datapoint) {
        // so just no exception
        datapoint.hashCode();
    }
    @Provide
    Arbitrary<Object> datapoints();
}
