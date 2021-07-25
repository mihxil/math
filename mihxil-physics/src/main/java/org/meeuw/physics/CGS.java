package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * @author Michiel Meeuwissen
 * @since .0.6
 */
public class CGS implements SystemOfMeasurements {
    @Override
    @NonNull
    public Unit forDimension(Dimension dimension) {
        throw new UnsupportedOperationException();
    }

    @Override
    @NonNull
    public Units forDimensions(DimensionalAnalysis dimension) {
        throw new UnsupportedOperationException();

    }

    enum CGSUnit implements Unit {
        ;
        @Getter
        private final Dimension dimension;

        @Getter
        private final UncertainReal SIFactor;

        CGSUnit(Dimension dimension, UncertainReal siFactor) {
            this.dimension = dimension;
            SIFactor = siFactor;
        }

        @Override
        public DimensionalAnalysis getDimensions() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }



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
