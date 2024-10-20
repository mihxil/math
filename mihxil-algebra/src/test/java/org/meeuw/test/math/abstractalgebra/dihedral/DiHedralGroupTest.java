package org.meeuw.test.math.abstractalgebra.dihedral;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.dihedral.DiHedralGroup;
import org.meeuw.math.abstractalgebra.dihedral.DiHedralSymmetry;
import org.meeuw.theories.abstractalgebra.GroupTheory;

public class DiHedralGroupTest implements GroupTheory<DiHedralSymmetry> {

    @Override
    public Arbitrary<? extends DiHedralSymmetry> elements() {
        return Arbitraries.of(DiHedralGroup.of(3).stream().toList());
    }
}
