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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Marker interface for a 'base' unit of the associated {@link SystemOfMeasurements}.
 *
 * It is a singular {@link Unit} and can be implemented as enums, because a {@link SystemOfMeasurements} normally has
 * a fixed number of {@link BaseUnit}s, one for each possible {@link Dimension}.
 */
public interface BaseUnit extends Unit {

    SystemOfMeasurements getSystem();

    Dimension getDimension();

    @Override
    default List<Quantity> getQuantities() {
        DimensionalAnalysis analysis = new DimensionalAnalysis(DimensionExponent.of(getDimension(), 1));
        return Quantity.getQuantities().stream()
            .filter(q -> q.getDimensionalAnalysis().equals(analysis))
            .collect(Collectors.toList());
    }

    @Override
    default DimensionalAnalysis getDimensions() {
        return DimensionalAnalysis.of(getDimension());
    }

    @Override
    default Units times(Units multiplier) {
        return Units.of(this).times(multiplier);
    }

    @Override
    default Units reciprocal() {
        return Units.of(this).reciprocal();
    }

    @Override
    default Unit withQuantity(Quantity... quantity) {
        return
            new DerivedUnit(this, this.getSymbol(), this.getDescription()).withQuantity(quantity);
    }
}
