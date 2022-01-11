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
