package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupTheory;

import static org.meeuw.physics.SI.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UnitsImplTest implements MultiplicativeGroupTheory<Units> {

    static Units[] units = {DISTANCE, LENGTH, AREA, VOLUME, TIME, SPEED, WEIGHT, TEMPERATURE, ELECTRIC_CURRENT, AMOUNT_OF_SUBSTANCE, LUMINOUS_INTENSITY};

    @Override
    public Arbitrary<Units> elements() {
        return Arbitraries.of(units);
    }
}
