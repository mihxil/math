/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.physics;

import lombok.Getter;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.CGS.CGSUnit.*;
import static org.meeuw.physics.Quantity.*;
import static org.meeuw.physics.UnitExponent.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class CGS implements SystemOfMeasurements {

    public static final CGS INSTANCE = new CGS();


    @Override
    @NonNull
    public Unit forDimension(Dimension dimension) {
        switch(dimension) {
            case L: return cm;
            case M: return g;
            case T: return s;
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

    public static final Units cmPerS = cm.per(s);

    // acceleration
    public static final DerivedUnit Gal =
        new DerivedUnit("Gal", "gal", of(cm, 1), of(s, -2))
            .withQuantity(ACCELERATION);

    // force
    public static final DerivedUnit dyne =  new DerivedUnit("dyn", "dyne", of(g, 1), of(cm, 1), of(s, -2))
        .withQuantity(FORCE);

    // energy
    public static final DerivedUnit erg =  new DerivedUnit("erg", "erg", of(g, 1), of(cm, 2), of(s, -2))
        .withQuantity(ENERGY);

}
