package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.GroupTheory;
import org.meeuw.math.abstractalgebra.trivial.TrivialElement;

public class TrivialTest implements GroupTheory<TrivialElement> {
    @Override
    public Arbitrary<? extends TrivialElement> elements() {
        return Arbitraries.of(TrivialElement.e);
    }
}
