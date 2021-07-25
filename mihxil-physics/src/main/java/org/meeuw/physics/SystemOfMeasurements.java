package org.meeuw.physics;

import org.checkerframework.checker.nullness.qual.NonNull;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements {

    @NonNull
    Unit forDimension(Dimension dimension);

    /**
     * Returns in this system of measurements the preferred unit of the given dimension
     */
    @NonNull
    default Units forDimensions(DimensionalAnalysis dimensionalAnalysis) {
        return new UnitsImpl(exactly(1), dimensionalAnalysis);

    }

    default Units forDimensions(DimensionExponent... dimensions) {
        return forDimensions(DimensionalAnalysis.of(dimensions));
    }
}
