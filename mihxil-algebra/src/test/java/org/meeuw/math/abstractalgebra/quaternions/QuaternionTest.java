package org.meeuw.math.abstractalgebra.quaternions;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.test.DivisionRingTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class QuaternionTest implements DivisionRingTheory<Quaternion<RationalNumber>> {

    static final Quaternions<RationalNumber> structure = Quaternions.of(RationalNumbers.INSTANCE);

    @Test
    public void ijksqr() {
        assertThat(structure.i().sqr()).isEqualTo(structure.one().negation());
        assertThat(structure.j().sqr()).isEqualTo(structure.one().negation());
        assertThat(structure.k().sqr()).isEqualTo(structure.one().negation());
        assertThat(structure.i().times(structure.j()).times(structure.k())).isEqualTo(structure.one().negation());
    }

    @Test
    public void testToString() {
        assertThat(structure.one().times(of(2)).toString()).isEqualTo("2");
        assertThat(structure.i().times(of(2)).toString()).isEqualTo("2i");
        assertThat(structure.j().times(of(2)).toString()).isEqualTo("2j");
        assertThat(new Quaternion<>(of(0), of(1), of(-2), of(3)).dividedBy(of(2)).toString()).isEqualTo("¹⁄₂i - j + ³⁄₂k");
    }

    @Property
    public void conjugate(@ForAll(ELEMENTS) Quaternion<RationalNumber> e) {
        assertThat(e.conjugate().conjugate()).isEqualTo(e);
    }

    @Property
    public void conjugate(@ForAll(ELEMENTS) Quaternion<RationalNumber> e1, @ForAll(ELEMENTS) Quaternion<RationalNumber> e2) {
        assertThat(e1.times(e2).conjugate()).isEqualTo(e2.conjugate().times(e1.conjugate()));
    }

    @Override
    public Arbitrary<? extends Quaternion<RationalNumber>> elements() {
        return Arbitraries.randoms().map(r ->
            new Quaternion<>(
                RationalNumbers.INSTANCE.nextRandom(r),
                RationalNumbers.INSTANCE.nextRandom(r),
                RationalNumbers.INSTANCE.nextRandom(r),
                RationalNumbers.INSTANCE.nextRandom(r)
            ));
    }
}
