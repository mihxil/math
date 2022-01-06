package org.meeuw.physics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.meeuw.physics.Dimension.*;
import static org.meeuw.physics.DimensionalAnalysis.of;

/**
 * A quantity combines a {@link DimensionalAnalysis} with an <em>interpretation</em>.
 *
 * This is similar in that is indicates whether values associated with it can be compared or added, but less absolute.
 * E.g. you may add a 'length' to a 'width' (to obtain e.g. a circumference), or to assert e.g. that 'the width is greater than the length' Where is is absolutely senseless to ever add a 'width' to a 'temperature').
 */
@Getter
@EqualsAndHashCode
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

    private final String name;
    private final String symbol;
    private final DimensionalAnalysis dimensionalAnalysis;

    public Quantity(String name, String symbol, DimensionalAnalysis dimensionalAnalysis) {
        this.name = name;
        this.symbol = symbol;
        this.dimensionalAnalysis = dimensionalAnalysis;
    }


}
