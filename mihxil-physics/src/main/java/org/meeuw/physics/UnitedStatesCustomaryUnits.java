package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

/**
 * In US, you may
 */
public class UnitedStatesCustomaryUnits implements SystemOfMeasurements {
    @Override
    public @NonNull Unit forDimension(Dimension dimension) {
        return null;
    }


    enum US implements Unit {
        yd(Dimension.L, "yard", exactly(0.9144)),
        lb(Dimension.M, "pound", exactly(0.45359237))
        ;
        US(Dimension dimension, String description, UncertainReal siFactor) {
            this.dimension = dimension;
            this.description = description;
            this.SIFactor = siFactor;
        }
        @Override
        public DimensionalAnalysis getDimensions() {
            return DimensionalAnalysis.of(dimension);
        }

        @Getter
        private final Dimension dimension;

        @Getter
        private final UncertainReal SIFactor;

        @Getter
        private final String description;

        @Override
        public Units reciprocal() {
            return null;
        }

        @Override
        public Units times(Units multiplier) {
            return null;
        }
    }


}
