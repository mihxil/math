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

import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * Packs a certain {@link Unit} with an exponent.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitExponent implements Comparable<UnitExponent> {

    @Getter
    final int exponent;

    @Getter
    final Unit unit;

    public UnitExponent(Unit unit, int exponent) {
        this.exponent = exponent;
        this.unit = unit;
    }

    public static UnitExponent of(Unit u, int exponent) {
        return new UnitExponent(u, exponent);
    }

    public DimensionalAnalysis getDimensions() {
        return unit.getDimensions().pow(exponent);
    }

    public UnitExponent pow(int d) {
        return of(unit, d * exponent);
    }

    public UnitExponent times(UnitExponent u) {
        if (!unit.equals(u.unit)) {
            throw new IllegalArgumentException();
        }
        return of(unit, exponent + u.exponent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitExponent that = (UnitExponent) o;

        if (exponent != that.exponent) return false;
        return unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        int result = exponent;
        result = 31 * result + unit.hashCode();
        return result;
    }

    @Override
    public int compareTo(UnitExponent o) {
        return unit.name().compareTo(o.unit.name());
    }

    @Override
    public String toString() {
        return unit.getSymbol() + (exponent != 1 ? TextUtils.superscript(exponent) : "");
    }

    UncertainReal getSIFactor() {
        return unit.getSIFactor().pow(exponent);
    }
}
