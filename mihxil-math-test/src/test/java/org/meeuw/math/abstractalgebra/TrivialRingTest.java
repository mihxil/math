package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.meeuw.math.abstractalgebra.test.RingTheory;
import org.meeuw.math.abstractalgebra.trivial.TrivialRingElement;

public class TrivialRingTest implements RingTheory<TrivialRingElement> {
    @Override
    public Arbitrary<? extends TrivialRingElement> elements() {
        return Arbitraries.of(TrivialRingElement.e);
    }
}
