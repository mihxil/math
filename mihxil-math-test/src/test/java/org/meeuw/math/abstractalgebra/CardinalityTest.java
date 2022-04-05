package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.MultiplicativeSemiGroupTheory;

public class CardinalityTest implements MultiplicativeSemiGroupTheory<Cardinality> {
    @Override
    public Arbitrary<? extends Cardinality> elements() {
        return Arbitraries.of(
            Cardinality.ALEPH_0,
            Cardinality.ALEPH_1,
            Cardinality.C,
            Cardinality.ONE,
            Cardinality.of(2),
            Cardinality.of(3),
            Cardinality.of(100)
        );
    }
}
