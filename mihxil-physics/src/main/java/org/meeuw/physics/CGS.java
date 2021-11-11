package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.CGS.CGSUnit.cm;
import static org.meeuw.physics.UnitExponent.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class CGS implements SystemOfMeasurements {

    public static CGS INSTANCE = new CGS();



    @Override
    @NonNull
    public Unit forDimension(Dimension dimension) {
        switch(dimension) {
            case L: return cm;
            case M: return CGSUnit.g;
            case T: return CGSUnit.s;
            case I: return SIUnit.A;
            case Θ: return SIUnit.K;
            case N: return SIUnit.mol;
            default:
                assert dimension == Dimension.J;
                return SIUnit.cd;
        }
    }


    enum CGSUnit implements BaseUnit {
        cm(Dimension.L, exactly(0.01)),
        g(Dimension.M, exactly(0.001)),
        s(Dimension.T, exactly(1)),

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
        public String getDescription() {
            return name();
        }

        @Override
        public SystemOfMeasurements getSystem() {
            return CGS.INSTANCE;
        }
    }

    // acceleration
    public static final DerivedUnit gal =  new DerivedUnit("Gal", "gal", of(cm, 1), of(CGSUnit.s, -2));

    // force
}
