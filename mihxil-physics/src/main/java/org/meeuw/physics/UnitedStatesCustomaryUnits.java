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
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;

/**
 * In US
 */
public class UnitedStatesCustomaryUnits implements SystemOfMeasurements {

    public static final UnitedStatesCustomaryUnits INSTANCE = new UnitedStatesCustomaryUnits();

    @Override
    public @NonNull Unit forDimension(Dimension dimension) {
        return switch (dimension) {
            case L -> US.yd;
            case M -> US.lb;
            case T -> SI.min;
            case N -> SIUnit.mol;
            case J -> SIUnit.cd;
            default -> throw new IllegalArgumentException();
        };
    }


    public enum US implements BaseUnit {
        yd(Dimension.L, "yard", exactly(0.9144)),
        lb(Dimension.M, "pound", exactly(0.45359237))
        ;
        US(Dimension dimension, String description, RealNumber siFactor) {
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
        private final RealNumber SIFactor;

        @Getter
        private final String description;

        @Override
        public Units reciprocal() {
            return DerivedUnit.builder()
                .name("/" + this)
                .exponents(getDimensions().reciprocal().getExponents())
                .siFactor(SIFactor.reciprocal())
                .build();
        }

        @Override
        public Units times(Units multiplier) {
            return DerivedUnit.builder()
                .name(this + multiplier.toString())
                .exponents(getDimensions().times(multiplier.getDimensions()).getExponents())
                .siFactor(SIFactor.times(multiplier.getSIFactor()))
                .build();
        }

        @Override
        public SystemOfMeasurements getSystem() {
            return UnitedStatesCustomaryUnits.INSTANCE;
        }
    }

    public static final DerivedUnit in =
        new DerivedUnit("in", "inch", exactly(1/36d), US.yd);

     public static final DerivedUnit ft =
        new DerivedUnit("ft", "feet", exactly(1/3d), US.yd);

    public static final DerivedUnit mi =
        new DerivedUnit("mi", "miles", exactly(1760d), US.yd);

}
