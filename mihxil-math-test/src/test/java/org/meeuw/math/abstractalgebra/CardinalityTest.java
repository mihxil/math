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
            new Cardinality(1),
            new Cardinality(2),
            new Cardinality(3),
            new Cardinality(100)
        );
    }
}
