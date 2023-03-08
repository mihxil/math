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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;

import org.meeuw.configuration.ReflectionUtils;

import static org.meeuw.physics.Dimension.*;
import static org.meeuw.physics.DimensionalAnalysis.of;

/**
 * A quantity combines a {@link DimensionalAnalysis} with an <em>interpretation</em> (with a {@link #getName() name} and a {@link #getSymbol() symbol})
 * <p>
 * This is still similar to {@code DimensionalAnalysis} in that it indicates whether values associated with it can be compared or added, but less absolute.
 * <p>
 * E.g. you might add a 'length' to a 'width' (to obtain a circumference), or to assert e.g. that 'the width is greater than the length' Where it is absolutely senseless to ever add a 'width' to a 'temperature'.
 * <p>
 * All constants (of the type {@code Quantity}) in this class are returned by {@link #getQuantities()}, but more can be registered
 * via {@link #registerQuantity(Quantity)};
 */
@Getter
@EqualsAndHashCode
@Log
public class Quantity {

    public static final Quantity DISTANCE = new Quantity("distance", "d", of(L));
    public static final Quantity LENGTH   = new Quantity("length", "l", of(L));
    public static final Quantity AREA     = new Quantity("area", "A", of(L, L));
    public static final Quantity VOLUME   = new Quantity("volume", "V", of(L, L, L));
    public static final Quantity DENSITY  = new Quantity("densitify", "ρ", of(M).dividedBy(VOLUME));
    public static final Quantity TIME     = new Quantity("time", "t", of(T));
    public static final Quantity VELOCITY = new Quantity("velocity", "v", of(L).dividedBy(of(T)));
    public static final Quantity ACCELERATION = new Quantity("acceleration", "a", VELOCITY.getDimensionalAnalysis().dividedBy(of(T)));

    public static final Quantity SPEED    = new Quantity("speed", "s", VELOCITY.getDimensionalAnalysis());
    public static final Quantity WEIGHT   = new Quantity("weight", "w", of(M));
    public static final Quantity MASS   = new Quantity("mass", "m", of(M));
    public static final Quantity TEMPERATURE         = new Quantity("temperature", "T", of(TH));
    public static final Quantity ELECTRIC_CURRENT    = new Quantity("current", "I", of(I));
    public static final Quantity AMOUNT_OF_SUBSTANCE = new Quantity("amount of substance", "N", of(N));
    public static final Quantity LUMINOUS_INTENSITY  = new Quantity("luminous intensity", "J", of(J));
    public static final Quantity FORCE   = new Quantity("force", "F", of(M, L, T.with(-2)));
    public static final Quantity ENERGY  = new Quantity("energy", "E", of(M, L.with(2), T.with(-2)));
    public static final Quantity POWER   = new Quantity("power", "P", ENERGY.getDimensionalAnalysis().dividedBy(of(T)));
    public static final Quantity SPECIFIC_HEAT   = new Quantity("specific heat", "", of(L.with(2),TH.with(-1), T.with(-2)));
    public static final Quantity FREQUENCY = new Quantity("frequency", "f", of(T.reciprocal()));
    public static final Quantity PRESSURE = new Quantity("pressure", "p", of(M, L.reciprocal(), T.with(-2)));

    public static final Quantity INFORMATION = new Quantity("information", "I", of());

    /**
     * A name (in english for this quantity)
     */
    private final String name;

    /**
     * The symbol normally used for this quantity (in physics). Like 'F' for 'force'.
     */
    private final String symbol;

    /**
     * @see DimensionalAnalysis
     */
    private final DimensionalAnalysis dimensionalAnalysis;

    public Quantity(String name, String symbol, DimensionalAnalysis dimensionalAnalysis) {
        this.name = name;
        this.symbol = symbol;
        this.dimensionalAnalysis = dimensionalAnalysis;
    }


    private static final List<Quantity> QUANTITIES = new ArrayList<>();
    static {
        ReflectionUtils.forConstants(Quantity.class, Quantity::registerQuantity);
    }

    /**
     * Returns a list of currently known quantities. New quantities can be registered with {@link #registerQuantity(Quantity)}
     */
    public static List<Quantity> getQuantities() {
        return Collections.unmodifiableList(QUANTITIES);
    }

    public static void registerQuantity(Quantity quantity) {
        QUANTITIES.add(quantity);
    }

}
