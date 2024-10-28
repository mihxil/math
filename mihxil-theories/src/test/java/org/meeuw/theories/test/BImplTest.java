package org.meeuw.theories.test;


import net.jqwik.api.*;


public class BImplTest implements BTheory<B> {

    @Override
    public  Arbitrary<B> datapoints() {
        return Combinators.combine(
            Arbitraries.integers().between(1, 100),
                Arbitraries.integers().between(1, 100))
            .as(BImpl::new);
    }
}
