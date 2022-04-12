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

import lombok.Data;

import org.meeuw.math.text.TextUtils;


/**
 * Bundles a {@link Dimension} with an exponent.
 */
public interface DimensionExponent {

    Dimension getDimension();

    int getExponent();

    static DimensionExponent of(Dimension e, int exponent) {
        if (exponent == 1) {
            return e;
        } else {
            return new Impl(e, exponent);
        }
    }

    default DimensionExponent with(int i) {
        return of(getDimension(), i);
    }

    default DimensionExponent reciprocal() {
        return of(getDimension(), getExponent() * - 1);
    }

    default UnitExponent toUnitExponent(SystemOfMeasurements systemOfMeasurements) {
        return UnitExponent.of(systemOfMeasurements.forDimension(getDimension()), getExponent());
    }

    @Data
    class Impl implements DimensionExponent {
        final Dimension dimension;
        final int exponent;

        @Override
        public String toString() {
            return TextUtils.toString(new Dimension[] {dimension}, new int[] {exponent});
        }
    }
}
