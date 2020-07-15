package org.meeuw.math.physics;

import org.meeuw.math.Group;

import static org.meeuw.math.physics.SIUnit.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Units extends Iterable<UnitExponent>, Group<Units> {

    Units DIMENSIONLESS = UnitsImpl.of();
    Units DISTANCE = UnitsImpl.of(m);
    Units LENGTH = UnitsImpl.of(m);
    Units AREA = UnitsImpl.of(m, m);
    Units VOLUME = UnitsImpl.of(m, m, m);
    Units TIME = UnitsImpl.of(s);
    Units SPEED = DISTANCE.dividedBy(TIME);

    Units WEIGHT = UnitsImpl.of(kg);
    Units TEMPERATURE = UnitsImpl.of(K);
    Units ELECTRIC_CURRENT = UnitsImpl.of(A);
    Units AMOUNT_OF_SUBSTANCE = UnitsImpl.of(mol);
    Units LUMINOUS_INTENSITY = UnitsImpl.of(cd);

    Dimensions dimensions();

    static boolean dimensionEquals(Units u1, Units u2) {
        if (u1 == null) {
            return u2 == null;
        }
        return u1.dimensions().equals(u2.dimensions());

    }

    static Units forAddition(Units u1, Units u2) {
        if (! dimensionEquals(u1, u2)) {
            throw new IllegalArgumentException();
        }
        return u1;
    }

    static Units forMultiplication(Units u1, Units u2) {
        Units newUnits = null;
        if (u1 != null) {
            newUnits = u1.times(u2);
        }
        return newUnits;
    }

    static Units forDivision(Units u1, Units u2) {
        Units newUnits = null;
        if (u1 != null) {
            newUnits = u1.dividedBy(u2);
        }
        return newUnits;
    }
    static Units forExponentiation(Units u, int e) {
        Units newUnits = null;
        if (u != null) {
            newUnits = u.pow(e);
        }
        return newUnits;
    }
    static Units forInversion(UnitsImpl u) {
        Units newUnits = null;
        if (u != null) {
            newUnits = u.inverse();
        }
        return newUnits;
    }


}
