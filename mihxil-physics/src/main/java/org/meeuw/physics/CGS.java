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
    public Unit forDimension(SIDimension dimension) {
        switch(dimension) {
            case L: return cm;
            case M: return g;
            case T: return s;
            case I: return SIUnit.A;
            case Θ: return SIUnit.K;
            case N: return SIUnit.mol;
            default:
                assert dimension == SIDimension.J;
                return SIUnit.cd;
    public Unit forDimension(Dimension dimension) {
        return switch (dimension) {
            case L -> cm;
            case M -> g;
            case T -> s;
            case I -> SIUnit.A;
            case Θ -> SIUnit.K;
            case N -> SIUnit.mol;
            default -> {
                assert dimension == Dimension.J;
                yield SIUnit.cd;
            }
        };
    }

    enum CGSUnit implements BaseUnit {
        cm(SIDimension.L, exactly(0.01)),
        g(SIDimension.M, exactly(0.001)),
        s(SIDimension.T, exactly(1)),

        ;
        @Getter
        private final SIDimension dimension;

        @Getter
        private final RealNumber SIFactor;

        CGSUnit(SIDimension dimension, UncertainReal siFactor) {
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
