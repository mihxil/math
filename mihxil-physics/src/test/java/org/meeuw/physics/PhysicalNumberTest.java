package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupTheory;

import static org.meeuw.physics.UnitsImplTest.units;


/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class PhysicalNumberTest implements MultiplicativeGroupTheory<PhysicalNumber> {




	@Override
	public Arbitrary<PhysicalNumber> elements() {
		return Arbitraries
			.randomValue(
				(random) -> new Measurement(
					random.nextDouble() * 100,
					Math.abs(random.nextDouble() * 10),
					units[random.nextInt(units.length)])
			);
/*
		return Arbitraries.of(
			new Measurement(0.6, 0.1, DISTANCE),
			PhysicalConstant.c,
			PhysicalNumbers.ONE
		).,*/
	}
}
