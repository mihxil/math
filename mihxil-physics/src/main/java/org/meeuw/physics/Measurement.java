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

import lombok.NonNull;

import org.meeuw.math.WithUnits;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.uncertain;

/**
 * A number with an uncertainty where the uncertainty is simply explicitly stated.
 * <p>
 * This is e.g. the single result of a measurement.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Measurement extends PhysicalNumber {


    /**
     * Just a shortcut to {@link #Measurement(double, double, Units)}, which can be statically imported.
     * <p>
     * So then you can type:
     * <pre>{@code
     *  import static org.meeuw.physics.Measurement.measurement;
     *  import static org.meeuw.physics.SIUnit.s;
     *  ..
     *  PhysicalNumber duration = measurement(2d, 0.1d, s)
     *  }
     * </pre>
     */
    public static Measurement measurement(double value, double uncertainty, Units units) {
        return new Measurement(value, uncertainty, units);
    }

    /**
     * Just a shortcut to {@link #Measurement(UncertainReal, Units)}, which can be statically imported.
     * @see #measurement(double, double, Units)
     */
    public static Measurement measurement(UncertainReal value, Units units) {
        return new Measurement(value, units);
    }

    public Measurement(double value, double uncertainty, Units units) {
        this(uncertain(value, uncertainty), units);
    }

    public Measurement(double value, double uncertainty, DerivedUnit derivedUnit) {
        this(uncertain(value, uncertainty), Units.of(derivedUnit));
    }

    public Measurement(UncertainReal wrapped, Units units) {
        super(wrapped, units);
    }

    public Measurement(UncertainDouble<?> wrapped, String units) {
        super(
            uncertain(wrapped.doubleValue(), wrapped.doubleUncertainty()),
            FormatService.fromString(units, Units.class)
        );
    }


    public Measurement(UncertainDouble<?> wrapped) {
        this(wrapped, wrapped instanceof WithUnits ? (((WithUnits) wrapped).getUnitsAsString()) : "");
    }

    public Measurement(UncertainDouble<?> wrapped, Units units) {
        super(uncertain(wrapped.doubleValue(), wrapped.doubleUncertainty()), units);
    }



    @Override
    protected Measurement copy(@NonNull UncertainReal wrapped, @NonNull Units units) {
        return new Measurement(wrapped, units);
    }

}
