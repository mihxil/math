package org.meeuw.theories.test;


import net.jqwik.api.*;

public class BTest implements ComparableTest<B> {

    @Override
    @Provide
    public  Arbitrary<Comparable<?>> comparables() {
        return Combinators.combine(
            Arbitraries.integers().between(1, 100),
                Arbitraries.strings().alpha().ofLength(5))
            .as(B::new);
    }
}
