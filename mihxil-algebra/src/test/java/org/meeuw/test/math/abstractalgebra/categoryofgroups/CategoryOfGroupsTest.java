package org.meeuw.test.math.abstractalgebra.categoryofgroups;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.categoryofgroups.Element;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.integers.ModuloRing;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.test.MultiplicativeSemiGroupTheory;

public class CategoryOfGroupsTest implements MultiplicativeSemiGroupTheory<Element> {


    @Override
    public Arbitrary<? extends Element> elements() {

        return Arbitraries.of(
            KleinGroup.INSTANCE,
            KleinGroup.INSTANCE.times(KleinGroup.INSTANCE),
            ModuloRing.of(5),
            Integers.INSTANCE

        );

    }
}
