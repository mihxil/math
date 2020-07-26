package org.meeuw.physics;

import org.meeuw.math.text.UncertainNumberFormat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitExponent {

    final int exponent;
    final Unit unit;

    public UnitExponent(Unit unit, int exponent) {
        this.exponent = exponent;
        this.unit = unit;
    }

    public static UnitExponent of(Unit u, int exponent) {
        return new UnitExponent(u, exponent);
    }

    public Dimensions getDimensions() {
        return unit.getDimensions().pow(exponent);
    }

    public UnitExponent pow(int d) {
        return of(unit, d * exponent);
    }
    public UnitExponent times(UnitExponent u) {
        if (!unit.equals(u.unit)) {
            throw new IllegalArgumentException();
        }
        return of(unit, exponent + u.exponent);
    }

    @Override
    public String toString() {
        return unit.name() + (exponent != 1 ? UncertainNumberFormat.superscript(exponent) : "");
    }

}
