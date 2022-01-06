package org.meeuw.physics;

import java.util.Iterator;

import java.util.List;

import org.meeuw.math.uncertainnumbers.field.UncertainReal;

public class PrefixedUnits implements Units {
    private final Units wrapped;

    private final Prefix prefix;

    public PrefixedUnits(Units wrapped, Prefix prefix) {
        this.wrapped = wrapped;
        this.prefix = prefix;
    }

    @Override
    public DimensionalAnalysis getDimensions() {
        return wrapped.getDimensions();
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
    public Units withQuantity(Quantity... quantity) {
        return new PrefixedUnits(wrapped.withQuantity(quantity), prefix);
    }

    @Override
    public Iterator<UnitExponent> iterator() {
        return wrapped.iterator();
    }

    @Override
    public Units reciprocal() {
        return wrapped.reciprocal().withPrefix(prefix.reciprocal().orElse(null));
    }

    @Override
    public Units times(Units multiplier) {
        return wrapped.times(multiplier).withPrefix(prefix);
    }

    @Override
    public String toString() {
        return prefix + wrapped.toString();

    }
}
