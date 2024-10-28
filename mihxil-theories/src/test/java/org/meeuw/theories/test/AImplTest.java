package org.meeuw.theories.test;

import net.jqwik.api.*;

public class AImplTest implements ATheory<AImpl> {
    @Override
    public Arbitrary<AImpl> datapoints() {
        return
            Arbitraries.integers().between(1, 100).map(AImpl::new);
    }
}
