package org.meeuw.physics;

import lombok.NonNull;

import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A number with a uncertainty where the uncertainty is simply explicitely stated.
 *
 * This is e.g. the single result of a measurement.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Measurement extends PhysicalNumber {

    public Measurement(double value, double uncertainty, Units units) {
        this(new UncertainDoubleElement(value, uncertainty), units);
    }
    public Measurement(double value, double uncertainty, DerivedUnit derivedUnit) {
        this(new UncertainDoubleElement(value, uncertainty), UnitsImpl.of(derivedUnit));
    }

    public Measurement(UncertainReal wrapped, Units units) {
        super(wrapped, units);
    }

    @Override
    protected Measurement copy(@NonNull UncertainReal wrapped, @NonNull Units units) {
        return new Measurement(wrapped, units);
    }

    @Override
    public PhysicalNumber reciprocal() {
        return pow(-1);
    }
}
