package org.meeuw.physics;

import lombok.Getter;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.*;

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

    @NonNull
    public  Units forDimensions(DimensionalAnalysis dimensionalAnalysis) {
        UnitExponent[] unitExponents = dimensionalAnalysis.stream().map(dimensionExponent -> dimensionExponent.toUnitExponent(this)).toArray(UnitExponent[]::new);
        final UncertainReal siFactor = Arrays.stream(unitExponents)
            .map(UnitExponent::getSIFactor)
            .reduce(UncertainRealField.INSTANCE.one(), UncertainReal::times);
        return new UnitsImpl(siFactor, unitExponents);
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
