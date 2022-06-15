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

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The representation of a singular 'unit'. This can be {@link BaseUnit} or a {@link DerivedUnit}
 *
 * It has one symbol {@link #getSymbol()} which for enums may simply be the same as {@link Enum#name()}.
 *
 * A singular unit can also be {@link #withPrefix(Prefix)} prefixed.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Unit extends Units {

    /**
     * @return the dimensional analysis for this unit
     */
    @Override
    DimensionalAnalysis getDimensions();

    String getDescription();

    String name();

    @Override
    default List<Quantity> getQuantities() {
        return Collections.emptyList();
    }

    default String getSymbol() {
        return name();
    }

    default Unit withPrefix(Prefix prefix) {
        return new PrefixedUnit(this, prefix);
    }

    @Override
    Unit withQuantity(Quantity... quantity);


    @Override
    default UnitExponent[] getCanonicalExponents() {
        return new UnitExponent[] {new UnitExponent(this, 1)};
    }

    @Override
    @NonNull
    default Iterator<UnitExponent> iterator() {
        return Arrays.stream(getCanonicalExponents()).iterator();
    }

    static UnitExponent[] toArray(Unit... units) {
        Map<Unit, AtomicInteger> map = new LinkedHashMap<>();
        for (Unit unit : units) {
            map.computeIfAbsent(unit, (u) -> new AtomicInteger(0)).incrementAndGet();
        }
        return map.entrySet()
            .stream()
            .map(e -> new UnitExponent(e.getKey(), e.getValue().intValue()))
            .toArray(UnitExponent[]::new);
    }

}
