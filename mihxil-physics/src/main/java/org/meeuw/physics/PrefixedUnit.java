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

import java.util.List;
import java.util.Objects;

import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A unit wrapping another {@link Unit} with a {@link Prefix}, and it is therefore just a multiple of that unit.
 */
public class PrefixedUnit implements Unit {

    private final Unit wrapped;

    @Getter
    private final Prefix prefix;

    public PrefixedUnit(Unit wrapped, Prefix prefix) {
        this.wrapped = wrapped;
        this.prefix = prefix;
    }

    @Override
    public DimensionalAnalysis getDimensions() {
        return wrapped.getDimensions();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public String name() {
        return prefix + wrapped.name();
    }

    @Override
    public UncertainReal getSIFactor() {
        return wrapped.getSIFactor().times(prefix.getAsDouble());
    }

    @Override
    public List<Quantity> getQuantities() {
        return wrapped.getQuantities();
    }

    @Override
    public Unit withQuantity(Quantity... quantity) {
        return new PrefixedUnit(wrapped.withQuantity(quantity), prefix);
    }

    @Override
    public Units reciprocal() {
        return new CompositeUnits(getSIFactor().reciprocal(), new UnitExponent(this, -1));
    }

    @Override
    public Units times(Units multiplier) {
        UnitExponent[] canonicalExponents = multiplier.getCanonicalExponents();
        UnitExponent[] exponents = new UnitExponent[canonicalExponents.length + 1];
        exponents[0] = new UnitExponent(this, 1);
        System.arraycopy(canonicalExponents, 0, exponents, 1, canonicalExponents.length);
        return new CompositeUnits(
            getSIFactor().times(multiplier.getSIFactor()),
            exponents
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrefixedUnit that = (PrefixedUnit) o;
        return Objects.equals(wrapped, that.wrapped) && Objects.equals(prefix, that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrapped, prefix);
    }

    @Override
    public String toString() {
        return prefix + wrapped.toString();
    }
}
