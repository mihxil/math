package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class Planck  implements SystemOfMeasurements {

    public static final Planck INSTANCE = new Planck();

    private Planck() {

    }


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


    public static final String sub = TextUtils.subscript("P");

    enum PlanckUnit implements BaseUnit {

        PlanckLength(L, "ℓ" + sub, new UncertainDoubleElement(1.616_255e-35, 0.000_018e-35)),
        PlanckMass(M, "m" + sub, new UncertainDoubleElement(2.176_434e-8, 0.000_024e-8)),
        PlanckTime(T, "t" + sub, new UncertainDoubleElement(5.391_247e-44, 0.000_060e-44)),
        PlanckTemperature(Θ, "T" + sub, new UncertainDoubleElement(1.416_784e32, 0.000_016e32)),
        PlanckCharge(I, "q" + sub, new UncertainDoubleElement(1.875_545_956e-18, 0.000_000_041e-18));

        @Getter
        private final Dimension dimension;

        @Getter
        private final UncertainReal SIFactor;

        @Getter
        private final String symbol;

        PlanckUnit(Dimension dimension, String symbol, UncertainReal siFactor) {
            this.dimension = dimension;
            this.symbol = symbol;
            this.SIFactor = siFactor;
        }

        @Override
        public String getDescription() {
            return name();
        }

        @Override
        public SystemOfMeasurements getSystem() {
            return Planck.INSTANCE;
        }
    }

    //public static PhysicalConstant PlanckCharge = new PhysicalConstant("q" + sub, new UncertainDoubleElement(1.875_545_956e-18, 0.000_000_041e-18), "Plank Charge");

    public static final PhysicalConstant e = new PhysicalConstant("e", exactly(1), PlanckUnit.PlanckCharge, "e");

    public static final PhysicalConstant c = new PhysicalConstant("c", exactly(1), PlanckUnit.PlanckLength.dividedBy(PlanckUnit.PlanckTime), "c");

    public static final PhysicalConstant kB = new PhysicalConstant("kB", exactly(1), PlanckUnit.PlanckLength.dividedBy(PlanckUnit.PlanckTime), "c");

}
