package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitExponent implements Comparable<UnitExponent> {

    @Getter
    final int exponent;

    @Getter
    final Unit unit;

    public UnitExponent(Unit unit, int exponent) {
        this.exponent = exponent;
        this.unit = unit;
    }

    public static UnitExponent of(Unit u, int exponent) {
        return new UnitExponent(u, exponent);
    }

    public DimensionalAnalysis getDimensions() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitExponent that = (UnitExponent) o;

        if (exponent != that.exponent) return false;
        return unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        int result = exponent;
        result = 31 * result + unit.hashCode();
        return result;
    }

    @Override
    public int compareTo(UnitExponent o) {
        return unit.name().compareTo(o.unit.name());
    }

    @Override
    public String toString() {
        return unit.name() + (exponent != 1 ? TextUtils.superscript(exponent) : "");
    }

}
