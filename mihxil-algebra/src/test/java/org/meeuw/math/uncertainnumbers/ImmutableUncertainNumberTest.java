package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ImmutableUncertainNumberTest implements SignedNumberTheory<UncertainReal> {

    @Override
    public Arbitrary<UncertainDoubleElement> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new UncertainDoubleElement(value, Math.abs(value * r.nextDouble()));
        });

    }
}
