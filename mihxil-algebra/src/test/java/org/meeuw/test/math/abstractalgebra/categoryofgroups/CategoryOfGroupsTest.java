package org.meeuw.test.math.abstractalgebra.categoryofgroups;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.integers.ModuloRing;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.product.ProductGroup;
import org.meeuw.math.abstractalgebra.test.MultiplicativeSemiGroupTheory;

@Log4j2
public class CategoryOfGroupsTest implements MultiplicativeSemiGroupTheory<Group<?>> {


    @Test
    public void basic() {
        ProductGroup times = KleinGroup.INSTANCE.times(ModuloRing.of(5));
        times.stream().forEach(ge -> {
            log.info("{}", ge);
        });

    }



    @Override
    public Arbitrary<? extends Group<?>> elements() {

        return Arbitraries.of(
            KleinGroup.INSTANCE,
            KleinGroup.INSTANCE.times(KleinGroup.INSTANCE),
            ModuloRing.of(5),
            Integers.INSTANCE
        );

    }
}
