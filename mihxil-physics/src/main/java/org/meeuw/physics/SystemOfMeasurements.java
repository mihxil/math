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

import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ReflectionUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

/**
 * There are different 'systems of measurements' in use. E.g. {@link SI}  and {@link Planck}.
 * <p>
 *
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements {

    @NonNull
    Unit forDimension(Dimension dimension);

    /**
     * Returns in this system of measurements the preferred units for the given dimensional analysis. This may not
     * always be unique.
     */
    @NonNull
    default Units forDimensions(DimensionalAnalysis dimensionalAnalysis) {
        UnitExponent[] unitExponents = dimensionalAnalysis.stream()
            .map(dimensionExponent -> dimensionExponent.toUnitExponent(this))
            .toArray(UnitExponent[]::new);
        final UncertainReal siFactor = Arrays.stream(unitExponents)
            .map(UnitExponent::getSIFactor)
            .reduce(UncertainRealField.INSTANCE.one(), UncertainReal::times);
        return new CompositeUnits(siFactor, unitExponents);
    }
    default Units forQuantity(Quantity quantity) {
        for (Units unit : getUnits()) {
            if (unit.getQuantities().contains(quantity)) {
                return unit;
            }
        }
        return forDimensions(quantity.getDimensionalAnalysis());
    }


    default List<BaseUnit> getBaseUnits() {
        List<BaseUnit> result = new ArrayList<>();
        ReflectionUtils.forConstants(this.getClass(), BaseUnit.class, result::add);
        return Collections.unmodifiableList(result);
    }

    default List<Units> getUnits()  {
        final List<Units> result = new ArrayList<>(getBaseUnits());
        ReflectionUtils.forConstants(this.getClass(), Units.class, result::add);
        return Collections.unmodifiableList(result);
    }



    default Units forDimensions(DimensionExponent... dimensions) {
        return forDimensions(DimensionalAnalysis.of(dimensions));
    }
}
