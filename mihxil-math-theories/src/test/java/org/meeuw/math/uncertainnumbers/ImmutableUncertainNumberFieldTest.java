package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.test.FieldTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ImmutableUncertainNumberFieldTest implements FieldTheory<UncertainNumberElement> {

    @Override
    public Arbitrary<ImmutableUncertainNumber> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new ImmutableUncertainNumber(value, Math.abs(value * r.nextDouble()));
        });

    }
}
