package org.meeuw.theories.test;


import net.jqwik.api.*;

public class ATest implements ObjectTest<A> {

    @Override
    @Provide
    public Arbitrary<Object> datapoints() {
        return Combinators.combine(
            Arbitraries.integers().between(1, 100),
                Arbitraries.strings().alpha().ofLength(5))
            .as(A::new);
    }
}
