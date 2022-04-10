package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.GroupTheory;
import org.meeuw.math.abstractalgebra.trivial.TrivialGroupElement;

public class TrivialGroupTest implements GroupTheory<TrivialGroupElement> {
    @Override
    public Arbitrary<? extends TrivialGroupElement> elements() {
        return Arbitraries.of(TrivialGroupElement.e);
    }
}
