package org.meeuw.physics;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.Streamable;
import org.meeuw.math.text.spi.FormatService;

import static org.meeuw.physics.Dimension.*;

/**
 * A dimensions object represent a physical dimensional analysis.
 *
 * It is basically a wrapper for an array of exponents, one for each possible {@link Dimension}
 *
 * @author Michiel Meeuwissen
 */
public class DimensionalAnalysis
    implements MultiplicativeGroupElement<DimensionalAnalysis>, Streamable<DimensionExponent> {

    public static final DimensionalAnalysis DISTANCE = of(L);
    public static final DimensionalAnalysis LENGTH   = of(L);
    public static final DimensionalAnalysis AREA     = of(L, L);
    public static final DimensionalAnalysis VOLUME   = of(L, L, L);
    public static final DimensionalAnalysis TIME     = of(T);
    public static final DimensionalAnalysis VELOCITY    = DISTANCE.dividedBy(TIME);
    public static final DimensionalAnalysis SPEED    = VELOCITY;
    public static final DimensionalAnalysis WEIGHT   = of(M);
    public static final DimensionalAnalysis TEMPERATURE         = of(TH);
    public static final DimensionalAnalysis ELECTRIC_CURRENT    = of(I);
    public static final DimensionalAnalysis AMOUNT_OF_SUBSTANCE = of(N);
    public static final DimensionalAnalysis LUMINOUS_INTENSITY  = of(J);

    public static final DimensionalAnalysis DENSITY  = DimensionalAnalysis.of(M).dividedBy(VOLUME);

    public static final DimensionalAnalysis ACCELERATION = VELOCITY.dividedBy(DimensionalAnalysis.of(T));
    public static final DimensionalAnalysis FORCE   = DimensionalAnalysis.of(M, L, T.with(-2));
    public static final DimensionalAnalysis ENERGY  = DimensionalAnalysis.of(M, L.with(2), T.with(-2));
    public static final DimensionalAnalysis POWER   = ENERGY.dividedBy(DimensionalAnalysis.of(T));
    public static final DimensionalAnalysis SPECIFIC_HEAT   = DimensionalAnalysis.of(L.with(2),TH.with(-1), T.with(-2));

    public static final DimensionalAnalysis FREQUENCY = DimensionalAnalysis.of(T.reciprocal());
    public static final DimensionalAnalysis PRESSURE = DimensionalAnalysis.of(M, L.reciprocal(), T.with(-2));


    private static final DimensionalAnalysis[] QUANTITIES;
    static {
        final List<DimensionalAnalysis> result = new ArrayList<>();
        for (Field f : DimensionalAnalysis.class.getDeclaredFields()) {
            if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()) && f.getType().equals(DimensionalAnalysis.class)) {
                try {
                    result.add((DimensionalAnalysis) f.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        QUANTITIES = result.toArray(new DimensionalAnalysis[0]);
    }
    public static DimensionalAnalysis[] getQuantities() {
        return QUANTITIES;
    }

    @Getter
    final int[] exponents = new int[Dimension.values().length];

    public DimensionalAnalysis(DimensionExponent... dimensions) {
        for (DimensionExponent v : dimensions) {
            exponents[v.getDimension().ordinal()] += v.getExponent();
        }
    }

    public DimensionalAnalysis(int[] exponents) {
        System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
    }

    public static DimensionalAnalysis of(DimensionExponent... dimensions) {
        return new DimensionalAnalysis(dimensions);
    }

    @Override
    public DimensionsGroup getStructure() {
        return DimensionsGroup.INSTANCE;
    }

    @Override
    public DimensionalAnalysis reciprocal() {
        return pow(-1);
    }

    @Override
    public DimensionalAnalysis times(DimensionalAnalysis multiplier) {
        DimensionalAnalysis copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] += multiplier.exponents[i];
        }
        return copy;
    }

    @Override
    public DimensionalAnalysis pow(int exponent) {
        DimensionalAnalysis copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] *= exponent;
        }
        return copy;
    }

    public DimensionalAnalysis copy() {
        return new DimensionalAnalysis(exponents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DimensionalAnalysis units = (DimensionalAnalysis) o;

        return Arrays.equals(exponents, units.exponents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }

    @Override
    public String toString() {
        return FormatService.toString(this);
    }

    @NonNull
    public Stream<DimensionExponent> stream() {
        return IntStream
            .range(0, Dimension.values().length)
            .mapToObj(i -> DimensionExponent.of(Dimension.values()[i], exponents[i]));
    }

}
