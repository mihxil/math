package org.meeuw.physics;

import lombok.Data;

import org.meeuw.math.text.TextUtils;


/**
 * Bundles a {@link Dimension} with an exponent.
 */
public interface DimensionExponent {

    Dimension getDimension();

    int getExponent();

    static DimensionExponent of(Dimension e, int exponent) {
        if (exponent == 1) {
            return e;
        } else {
            return new Impl(e, exponent);
        }
    }

    default DimensionExponent with(int i) {
        return of(getDimension(), i);
    }

    default DimensionExponent reciprocal() {
        return of(getDimension(), getExponent() * - 1);
    }

    default UnitExponent toUnitExponent(SystemOfMeasurements systemOfMeasurements) {
        return UnitExponent.of(systemOfMeasurements.forDimension(getDimension()), getExponent());
    }

    @Data
    class Impl implements DimensionExponent {
        final Dimension dimension;
        final int exponent;

        @Override
        public String toString() {
            return TextUtils.toString(new Dimension[] {dimension}, new int[] {exponent});
        }
    }
}
