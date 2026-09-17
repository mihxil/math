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

import lombok.Data;

import org.meeuw.math.text.TextUtils;


/**
 * Bundles a {@link Dimension} with an exponent.
 */
public interface DimensionExponent<D extends Dimension> {

    D getDimension();

    int getExponent();

    static <D extends Dimension> DimensionExponent<D> of(D e, int exponent) {
        return new Impl<D>(e, exponent);
    }

    default DimensionExponent<D> with(int i) {
        return of(getDimension(), i);
    }

    default DimensionExponent<D> reciprocal() {
        return of(getDimension(), getExponent() * - 1);
    }

    default UnitExponent toUnitExponent(SystemOfMeasurements<D> systemOfMeasurements) {
        return UnitExponent.of(systemOfMeasurements.forDimension(getDimension()), getExponent());
    }

    @Data
    class Impl<D extends Dimension> implements DimensionExponent<D> {
        final D dimension;
        final int exponent;

        @Override
        public String toString() {
            return TextUtils.toString(new Object[] {dimension}, new int[] {exponent});
        }
    }
}
