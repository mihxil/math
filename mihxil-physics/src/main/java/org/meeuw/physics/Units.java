package org.meeuw.physics;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exact;

/**
 * The representation of the units of a certain value.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Units extends Iterable<UnitExponent>, MultiplicativeGroupElement<Units> {

    Units DIMENSIONLESS = of(exact(1));

    @Override
    default UnitsGroup getStructure() {
        return UnitsGroup.INSTANCE;
    }

    default Prefix getPrefix() {
        return Prefix.NONE;
    }

    static UnitsImpl of(UncertainReal siFactor, Unit... units) {
        return new UnitsImpl(siFactor, units);
    }

    static UnitsImpl of(Unit... units) {
        UncertainReal factor = UncertainRealField.INSTANCE.one();
        for (Unit u : units) {
            factor = factor.times(u.getSIFactor());
        }
        return new UnitsImpl(factor, units);
    }

    Dimensions getDimensions();

    /**
     * Returns a constant representing a 0 with this units.
     */
    default PhysicalConstant zero() {
        return new PhysicalConstant("0", 0, this, "zero " + toString());
    }

    UncertainReal getSIFactor();

    default UncertainReal conversionFactor(Units units) {
        if (! Units.dimensionEquals(this, units)) {
            throw new DimensionsMismatchException("" + this + " cannot be converted to " + units);
        }
        return getSIFactor().dividedBy(units.getSIFactor());
    }

    static boolean dimensionEquals(Units u1, Units u2) {
        if (u1 == null || u2 == null) {
            return u2 == null && u1 == null;
        }
        return u1.getDimensions().equals(u2.getDimensions());
    }

    static boolean equals(Units u1, Units u2) {
        return dimensionEquals(u1, u2) && u1.getSIFactor().equals(u2.getSIFactor());
    }

    static boolean equals(Units u1, Object o) {
        return o instanceof Units && equals(u1, (Units) o);
    }

    /**
     * Returns a new Units object suitable for the result of adding the two quantities with given units.
     *
     * @return the first parameter if it is compatible with the second parameter
     * @throws DimensionsMismatchException if the two dimensions are not {@link #dimensionEquals(Units, Units)}
     */
    static Units forAddition(Units u1, Units u2) {
        if (! dimensionEquals(u1, u2)) {
            throw new DimensionsMismatchException("Cannot add [" + (u1 == null ? "null" : u1.getDimensions()) + "] to [" + (u2 == null ? "null" : u2.getDimensions()) + "]");
        }
        return u1;
    }

    static Units forMultiplication(Units u1, Units u2) {
        Units newUnits = null;
        if (u1 != null && u2 != null) {
            newUnits = u1.times(u2);
        }
        return newUnits;
    }

    static Units forDivision(Units u1, Units u2) {
        Units newUnits = null;
        if (u1 != null && u2 != null) {
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
