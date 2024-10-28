package org.meeuw.theories.test;


import net.jqwik.api.*;


public class BImplTest implements BTheory<B> {

    @Override
    @Provide("datapoints")
    public  Arbitrary<B> datapoints() {
        return
            Arbitraries.integers().between(1, 100).map(BImpl::new);
    }
}
