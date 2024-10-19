package org.meeuw.theories.test;


import net.jqwik.api.*;

/**
 * Every comparable is an object
 */
interface ComparableTest<X extends Comparable<?>> extends ObjectTest<X> {


    @Override
    @Provide
    default Arbitrary<Object> datapoints() {
        return comparables().asGeneric();
    }

    @Property
    default void compareToSelf(@ForAll("comparables") Comparable<Object> a) {
        a.compareTo(a);

    }
    @Property
    default void hashCode(@ForAll("datapoints") Object o) {
        // overriden
    }

    @Provide
    Arbitrary<Comparable<?>> comparables();
}
