package org.meeuw.physics;

import lombok.NonNull;

import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A number with an uncertainty where the uncertainty is simply explicitly stated.
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
        this(new UncertainDoubleElement(value, uncertainty), Units.of(derivedUnit));
    }

    public Measurement(UncertainReal wrapped, Units units) {
        super(wrapped, units);
    }


    public Measurement(UncertainDouble<?> wrapped, Units units) {
        super(new UncertainDoubleElement(wrapped.getValue(), wrapped.getUncertainty()), units);
    }

    @Override
    protected Measurement copy(@NonNull UncertainReal wrapped, @NonNull Units units) {
        return new Measurement(wrapped, units);
    }

}
