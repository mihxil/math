package org.meeuw.physics;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements {

    @NonNull
    Unit forDimension(Dimension dimension);

    /**
     * Returns in this system of measurements the preferred units for the given dimensional analysis
     */
    @NonNull
    Units forDimensions(DimensionalAnalysis dimensionalAnalysis);


    default Units forDimensions(DimensionExponent... dimensions) {
        return forDimensions(DimensionalAnalysis.of(dimensions));
    }
}
