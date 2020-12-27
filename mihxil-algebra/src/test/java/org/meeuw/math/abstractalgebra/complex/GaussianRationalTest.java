package org.meeuw.math.abstractalgebra.complex;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.test.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class GaussianRationalTest implements FieldTheory<GaussianRational> {

    static final GaussianRationals structure = GaussianRationals.INSTANCE;

    @Test
    public void string() {
        assertThat(structure.zero().toString()).isEqualTo("0");
        assertThat(structure.one().toString()).isEqualTo("1");
        assertThat(structure.one().negation().toString()).isEqualTo("-1");
        assertThat(structure.i().toString()).isEqualTo("i");
        assertThat(structure.i().negation().toString()).isEqualTo("-i");
        assertThat(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3))).toString()).isEqualTo("3i");
        assertThat(structure.one().plus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 + 3i");
        assertThat(structure.one().minus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 - 3i");

    }

    @Test
    public void isqr() {
        assertThat(structure.i().sqr()).isEqualTo(structure.one().negation());
    }

    @Override
    public Arbitrary<GaussianRational> elements() {
        return Arbitraries.of(
            new GaussianRational(RationalNumber.of(3), RationalNumber.ZERO),
            structure.i(),
            new GaussianRational(RationalNumber.of(3), RationalNumber.of(-2))
        );
    }
}
