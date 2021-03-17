package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * @author Michiel Meeuwissen
 * @since .0.6
 */
public class CGS implements SystemOfMeasurements{
    @Override
    public Unit forDimension(Dimension dimension) {
        return null;
    }

    @Override
    public Units forDimensions(Dimensions dimension) {
        return null;
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
        public Dimensions getDimensions() {
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
