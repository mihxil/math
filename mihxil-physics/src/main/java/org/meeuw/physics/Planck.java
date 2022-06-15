/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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

import static org.meeuw.math.text.TextUtils.subscript;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.uncertain;
import static org.meeuw.physics.Dimension.*;
import static org.meeuw.physics.Planck.PlanckUnit.*;

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
            case L: return PlanckLength;
            case M: return PlanckMass;
            case T: return PlanckTime;
            case I: return new DerivedUnit(PlanckCharge.dividedBy(PlanckTime),
                "Plank Current", "Planck Current");
            case Θ: return PlanckTemperature;
            case N: return SIUnit.mol;
            case J: return SIUnit.cd;
            default:
                throw new IllegalArgumentException();
        }
    }


    public static final String sub = subscript("P");

    enum PlanckUnit implements BaseUnit {

        PlanckLength(
            L, "ℓ" + sub,
            uncertain(1.616_255e-35, 0.000_018e-35)
        ),
        PlanckMass(
            M, "m" + sub,
            uncertain(2.176_434e-8, 0.000_024e-8)
        ),
        PlanckTime(
            T, "t" + sub,
            uncertain(5.391_247e-44, 0.000_060e-44)
        ),
        PlanckTemperature(
            Θ, "T" + sub,
            uncertain(1.416_784e32, 0.000_016e32)
        ),
        PlanckCharge(I, "q" + sub,
            uncertain(1.875_545_956e-18, 0.000_000_041e-18)
        );

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


    /**
     * Electron charge is 1 by definition
     */
    public static final PhysicalConstant e = new PhysicalConstant(
        "e",
        exactly(1),
        PlanckCharge,
        "e"
    );

    /**
     * Light speed in Planck units. 1 by definition.
     */
    public static final PhysicalConstant c = new PhysicalConstant(
        "c",
        exactly(1),
        PlanckLength.dividedBy(PlanckTime),
        "c"
    );

    /**
     * Boltzmann constant. 1 by definition.
     */
    public static final PhysicalConstant kB = new PhysicalConstant(
        "k" + subscript("B"),
        exactly(1),
        PlanckLength.dividedBy(PlanckTime),
        "Boltzmann constant"
    );

}
