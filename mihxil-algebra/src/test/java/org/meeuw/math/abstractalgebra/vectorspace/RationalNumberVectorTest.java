package org.meeuw.math.abstractalgebra.vectorspace;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.test.VectorSpaceTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RationalNumberVectorTest implements
    VectorSpaceTheory<Vector<RationalNumber>, RationalNumber>,
    WithScalarTheory<Vector<RationalNumber>, RationalNumber>
{


    @Test
    public void space() {
        VectorSpace<RationalNumber> space4 = VectorSpace.of(4, INSTANCE);
        VectorSpace<RationalNumber> space3 = VectorSpace.of(3, INSTANCE);
        assertThat(space3.equals(space4)).isFalse();
        assertThat(space3.equals(space3)).isTrue();
        assertThat(space3.hashCode() == new VectorSpace<>(3, INSTANCE).hashCode()).isTrue();

    }

    @Override
    public Arbitrary<? extends Vector<RationalNumber>> elements() {
        return Arbitraries.randomValue(INSTANCE::nextRandom)
            .tuple3().map((t) -> Vector.of(t.get1(), t.get2(), t.get3()));
    }

    @Override
    public Arbitrary<RationalNumber> scalars() {
        return Arbitraries
            .randomValue(INSTANCE::nextRandom)
            .injectDuplicates(1)
            .edgeCases(config -> {
                config.add(RationalNumber.ONE);
                config.add(RationalNumber.ZERO);
            });
    }
}
