/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.physics;

import java.util.List;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

/**
 * The representation of the physical units of a value.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Units extends
    Iterable<UnitExponent>,
    MultiplicativeGroupElement<Units> {

    Units DIMENSIONLESS = of(exactly(1));

    @Override
    default UnitsGroup getStructure() {
        return UnitsGroup.INSTANCE;
    }

    static CompositeUnits of(UncertainReal siFactor, Unit... units) {
        return new CompositeUnits(siFactor, units);
    }

    static CompositeUnits of(Unit... units) {
        UncertainReal factor = UncertainRealField.INSTANCE.one();
        for (Unit u : units) {
            factor = factor.times(u.getSIFactor());
        }
        return new CompositeUnits(factor, units);
    }

    DimensionalAnalysis getDimensions();

    /**
     * Returns a constant representing a 0 with this units.
     */
    default PhysicalConstant zero() {
        return new PhysicalConstant("0", 0, this, "zero " + this);
    }

    default Units per(Units units) {
        return dividedBy(units);
    }

    UncertainReal getSIFactor();

    default Units withQuantity(Quantity... quantity) {
        return new CompositeUnits(
            this.getSIFactor(),
            this.getCanonicalExponents()).withQuantity(quantity);
    }

    UnitExponent[] getCanonicalExponents();

    List<Quantity> getQuantities();

    default Units withName(String name) {
        return new DerivedUnit(this, name, null);
    }

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
