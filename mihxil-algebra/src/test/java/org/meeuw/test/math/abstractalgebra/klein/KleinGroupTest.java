package org.meeuw.test.math.abstractalgebra.klein;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.klein.KleinElement;
import org.meeuw.math.abstractalgebra.test.GroupTheory;

public class KleinGroupTest implements GroupTheory<KleinElement> {
    @Override
    public Arbitrary<? extends KleinElement> elements() {
        return Arbitraries.of(KleinElement.values());
    }


}
