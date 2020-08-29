package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.NumberTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ImmutableUncertainNumberTest implements NumberTheory<UncertainDouble> {

    @Override
    public Arbitrary<ImmutableUncertainDouble> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new ImmutableUncertainDouble(value, Math.abs(value * r.nextDouble()));
        });

    }
}
