package org.meeuw.math.abstractalgebra.complex;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.meeuw.math.abstractalgebra.FieldTheory;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ComplexNumberTest implements FieldTheory<ComplexNumber<RationalNumber>> {

	ComplexNumbers<RationalNumber> structure = new ComplexNumbers<>(RationalNumbers.INSTANCE);

	@Override
	public Arbitrary<ComplexNumber<RationalNumber>> elements() {
		return Arbitraries.of(
				ComplexNumber.of(RationalNumber.of(3), RationalNumber.ZERO),
				structure.i(),
				ComplexNumber.of(RationalNumber.of(3), RationalNumber.of(-2))
		);
	}
}
