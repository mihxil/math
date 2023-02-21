package org.meeuw.test.math.abstractalgebra.categoryofgroups;

import lombok.extern.log4j.Log4j2;

import java.util.Random;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.integers.*;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.product.ProductGroup;
import org.meeuw.math.abstractalgebra.quaternions.Quaternions;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
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

    @Test
    public void infinite() {
        Random random = new Random(1);
        ProductGroup times = EvenIntegers.INSTANCE.times(Quaternions.of(RationalNumbers.INSTANCE));
        times.stream().limit(10).forEach(ge -> {
            log.info("{}", ge);
        });
        for (int i = 0; i < 100; i++) {
            log.info(times.nextRandom(random));
        }

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
