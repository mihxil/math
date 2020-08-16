package org.meeuw.math.abstractalgebra.complex;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.test.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ComplexNumberTest implements FieldTheory<ComplexNumber<RationalNumber>> {

    static final ComplexNumbers<RationalNumber> structure = ComplexNumbers.of(RationalNumbers.INSTANCE);

    @Test
    public void string() {
        assertThat(structure.zero().toString()).isEqualTo("0");
        assertThat(structure.one().toString()).isEqualTo("1");
        assertThat(structure.i().toString()).isEqualTo("i");
        assertThat(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3))).toString()).isEqualTo("3i");
        assertThat(structure.one().plus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 + 3i");

    }

    @Override
    public Arbitrary<ComplexNumber<RationalNumber>> elements() {
        return Arbitraries.of(
            ComplexNumber.of(RationalNumber.of(3), RationalNumber.ZERO),
            structure.i(),
            ComplexNumber.of(RationalNumber.of(3), RationalNumber.of(-2))
        );
    }
}
