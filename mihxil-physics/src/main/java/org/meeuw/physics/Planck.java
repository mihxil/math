package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class Planck  implements SystemOfMeasurements {
    @Override
    @NonNull
    public Unit forDimension(Dimension dimension) {
        switch(dimension) {
            case L: return PlanckUnit.PlanckLength;
            case M: return PlanckUnit.PlanckMass;
            case T: return PlanckUnit.PlanckTime;
            case I: return new DerivedUnit(PlanckUnit.PlanckCharge.dividedBy(PlanckUnit.PlanckTime), "Plank Current", "Planck Current");
            case Θ: return PlanckUnit.PlanckTemperature;
            case N: return SIUnit.mol;
            case J: return SIUnit.cd;
        }
        throw new IllegalArgumentException();
    }

    enum PlanckUnit implements Unit {
        PlanckLength(L, new UncertainDoubleElement(1.616_255e-35, 0.000_018e-35)),
        PlanckMass(M, new UncertainDoubleElement(2.176_434e-8, 0.000_024e-8)),
        PlanckTime(T, new UncertainDoubleElement(5.391_247e-44, 0.000_060e-44)),
        PlanckTemperature(Θ, new UncertainDoubleElement(1.416_784e32, 0.000_016e32)),
        PlanckCharge(I, new UncertainDoubleElement(1.875_545_956e-18, 0.000_000_041e-18));

        @Getter
        private final Dimension dimension;

        @Getter
        private final UncertainReal SIFactor;

        PlanckUnit(Dimension dimension, UncertainReal siFactor) {
            this.dimension = dimension;
            SIFactor = siFactor;
        }

        @Override
        public DimensionalAnalysis getDimensions() {
            return DimensionalAnalysis.of(dimension);
        }

        @Override
        public String getDescription() {
            return name();
        }

        @Override
        public Units times(Units multiplier) {
            return Units.of(this).times(multiplier);
        }

        @Override
        public Units reciprocal() {
            return Units.of(this).reciprocal();
        }
    }
}
