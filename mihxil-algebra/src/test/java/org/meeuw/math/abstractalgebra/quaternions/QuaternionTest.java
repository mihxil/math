package org.meeuw.math.abstractalgebra.quaternions;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.DivisionRingTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class QuaternionTest implements
    DivisionRingTheory<Quaternion<RationalNumber>>,
    WithScalarTheory<Quaternion<RationalNumber>, RationalNumber> {

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
        assertThat(structure.zero().toString()).isEqualTo("0");
        assertThat(structure.one().times(of(2)).toString()).isEqualTo("2");
        assertThat(structure.i().times(of(2)).toString()).isEqualTo("2i");
        assertThat(structure.j().times(of(2)).toString()).isEqualTo("2j");
        assertThat(new Quaternion<>(of(0), of(1), of(-2), of(3)).dividedBy(of(2)).toString()).isEqualTo("¹⁄₂i - j + ³⁄₂k");
    }

    @Test
    public void abs() {
        assertThatThrownBy(() -> new Quaternion<>(RationalNumber.of(1), RationalNumber.of(1), RationalNumber.of(1), RationalNumber.of(1)).abs()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(new Quaternion<>(RealNumber.of(1), RealNumber.of(1), RealNumber.of(1), RealNumber.of(1)).abs()).isEqualTo(RealNumber.of(2));
    }

    @Property
    public void conjugateOfConjugateIsSelf(@ForAll(ELEMENTS) Quaternion<RationalNumber> e) {
        assertThat(e.conjugate().conjugate()).isEqualTo(e);
    }

    @Property
    public void conjugateOfProductIsProductOfConjugates(@ForAll(ELEMENTS) Quaternion<RationalNumber> e1,
                                                        @ForAll(ELEMENTS) Quaternion<RationalNumber> e2) {
        assertThat(e1.times(e2).conjugate()).isEqualTo(e2.conjugate().times(e1.conjugate()));
    }

    @Property
    public void elementClass(@ForAll(ELEMENT) Quaternion<RationalNumber> e) {
        assertThat(e.getStructure().getElementStructure()).isEqualTo(e.getA().getStructure());
        assertThat(e.getStructure().getElementStructure()).isEqualTo(e.getB().getStructure());
        assertThat(e.getStructure().getElementStructure()).isEqualTo(e.getC().getStructure());
        assertThat(e.getStructure().getElementStructure()).isEqualTo(e.getD().getStructure());
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

    @Override
    public Arbitrary<RationalNumber> scalars() {
        return Arbitraries.of(
            RationalNumber.of(1),
            RationalNumber.of(0),
            RationalNumber.of(2),
            RationalNumber.of(-1)
        );
    }
}
