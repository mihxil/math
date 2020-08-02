package org.meeuw.physics;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Units extends Iterable<UnitExponent>, MultiplicativeGroupElement<Units> {

    Units DIMENSIONLESS = UnitsImpl.of();

    Dimensions dimensions();

    PhysicalConstant zero();

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
    static Units forInversion(Units u) {
        Units newUnits = null;
        if (u != null) {
            newUnits = u.reciprocal();
        }
        return newUnits;
    }


}
