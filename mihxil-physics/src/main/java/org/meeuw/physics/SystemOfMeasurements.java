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
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.reals.RealField;

/**
 * There are different 'systems of measurements' in use. E.g. {@link SI}  and {@link Planck}.
 * <p>
 *
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements<D extends Dimension> {

    @NonNull
    Unit<D> forDimension(D dimension);

    /**
     * Returns in this system of measurements the preferred units for the given dimensional analysis. This may not
     * always be unique.
     */
    @NonNull
    default Units forDimensions(DimensionalAnalysis<D> dimensionalAnalysis) {
        UnitExponent[] unitExponents = dimensionalAnalysis.stream()
            .map(dimensionExponent -> dimensionExponent.<D>toUnitExponent(this))
            .toArray(UnitExponent[]::new);
        final RealNumber siFactor = Arrays.stream(unitExponents)
            .map(UnitExponent::getSIFactor)
            .reduce(RealField.INSTANCE.one(), RealNumber::times);
        return new CompositeUnits<>(siFactor, unitExponents);
    }

    default Units<D> forQuantity(Quantity quantity) {
        for (Units<D> unit : getUnits()) {
            if (unit.getQuantities().contains(quantity)) {
                return unit;
            }
        }
        return forDimensions(quantity.getDimensionalAnalysis());
    }


    default List<? extends BaseUnit<D>> getBaseUnits() {
        List<BaseUnit<D>> result = new ArrayList<>();
        ReflectionUtils.forConstants(this.getClass(), BaseUnit.class, result::add);
        return Collections.unmodifiableList(result);
    }

    default List<Units<D>> getUnits()  {
        final List<Units<D>> result = new ArrayList<>(getBaseUnits());
        ReflectionUtils.forConstants(this.getClass(), Units.class, result::add);
        return Collections.unmodifiableList(result);
    }

    default Units<D> forDimensions(DimensionExponent<D>... dimensions) {
        return forDimensions(DimensionalAnalysis.of(dimensions));
    }

    default Units<D> unitsOf(String s) {
        for (Units<D> u : getUnits()) {
            if (s.equals(u.toString())) {
                return u;
            }
        }
        throw new IllegalArgumentException(String.format("No units %s found in %s", s, getClass()));
    }
}
