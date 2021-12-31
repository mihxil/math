package org.meeuw.physics;

/**
 * Marker interface
 */
public interface BaseUnit extends Unit {

    SystemOfMeasurements getSystem();

    Dimension getDimension();

    @Override
    default DimensionalAnalysis getDimensions() {
        return DimensionalAnalysis.of(getDimension());
    }

    @Override
    default Units times(Units multiplier) {
        return Units.of(this).times(multiplier);
    }

    @Override
    default Units reciprocal() {
        return Units.of(this).reciprocal();
    }
}
