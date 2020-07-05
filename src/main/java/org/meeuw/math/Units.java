package org.meeuw.math;

import static org.meeuw.math.SIUnits.*;

/**
 * Represents the units of a {@link UncertainNumber}.
 *
 * Basicly keeps track of a integer power for each of the basic SI units.
 *
 * @author Michiel Meeuwissen
 */
public class Units extends AbstractEnumInts<SIUnits, Units> {

    public static final Units DIMENSIONLESS = Units.of();

    public static final Units DISTANCE = Units.of(m);
    public static final Units LENGTH = Units.of(m);
    public static final Units AREA = Units.of(m, m);
    public static final Units VOLUME = Units.of(m, m, m);
    public static final Units TIME = Units.of(s);
    public static final Units SPEED = DISTANCE.dividedBy(TIME);

    public static final Units WEIGHT = Units.of(kg);
    public static final Units TEMPERATURE = Units.of(K);
    public static final Units ELECTRIC_CURRENT = Units.of(A);
    public static final Units AMOUNT_OF_SUBSTANCE = Units.of(mol);
    public static final Units LUMINOUS_INTENSITY = Units.of(cd);


    public Units(SIUnits... units) {
        super(SIUnits.values(), units);
    }

    public static Units of(SIUnits... units) {
        return new Units(units);
    }

    public Dimensions dimensions() {
        return new Dimensions(exponents);
    }

    public String toString() {
        return Utils.toString(SIUnits.values(), exponents);
    }

    @Override
    Units newInstance() {
        return new Units();

    }
}
