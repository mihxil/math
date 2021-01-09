package org.meeuw.math.test;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainRealFieldTest implements CompleteFieldTheory<UncertainReal> {


    @Override
    public Arbitrary<? extends UncertainReal> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new UncertainDoubleElement(value, Math.abs(value * r.nextDouble()));
        });
    }
}
