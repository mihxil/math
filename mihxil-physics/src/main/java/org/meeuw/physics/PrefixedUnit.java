package org.meeuw.physics;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

import org.meeuw.math.uncertainnumbers.field.UncertainReal;

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
        UnitExponent[] canonicalExponents = wrapped.reciprocal().getCanonicalExponents();
        for (int i = 0; i < canonicalExponents.length; i++) {
            canonicalExponents[i] = new UnitExponent
                (canonicalExponents[i].getUnit(), -1 * canonicalExponents[i].getExponent());
        }
        return new CompositeUnits(wrapped.getSIFactor().reciprocal(), canonicalExponents);
    }

    @Override
    public Units times(Units multiplier) {
        return wrapped.times(multiplier);
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
