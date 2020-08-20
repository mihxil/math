package org.meeuw.math.abstractalgebra.vectorspace;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.test.VectorSpaceTheory;

import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class VectorImplTest implements VectorSpaceTheory<RationalNumber, VectorImpl<RationalNumber>> {


    @Override
    public Arbitrary<? extends VectorImpl<RationalNumber>> vectors() {
        return Arbitraries.randomValue(INSTANCE::nextRandom).tuple3().map((t) -> VectorImpl.of(t.get1(), t.get2(), t.get3()));
    }

    @Provide
    public Arbitrary<RationalNumber> elements() {
        return Arbitraries.randomValue(INSTANCE::nextRandom);
    }
}
