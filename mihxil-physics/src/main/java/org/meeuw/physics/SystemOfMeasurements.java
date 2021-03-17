package org.meeuw.physics;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements {


    Unit forDimension(Dimension dimension);

    /**
     * Returns in this system of measurements the preferred unit of the given dimension
     */
    Units forDimensions(Dimensions dimension);
}
