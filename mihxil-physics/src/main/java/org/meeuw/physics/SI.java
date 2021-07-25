package org.meeuw.physics;

import lombok.Getter;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Utils;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.DimensionalAnalysis.*;
import static org.meeuw.physics.SIUnit.*;
import static org.meeuw.physics.UnitExponent.of;

/**
 * The International System of Units
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class SI implements SystemOfMeasurements {

    public static final SI INSTANCE = new SI();

    public static final Units DISTANCE = INSTANCE.forDimensions(DimensionalAnalysis.DISTANCE);
    public static final Units LENGTH   = INSTANCE.forDimensions(DimensionalAnalysis.LENGTH);
    public static final Units AREA     = INSTANCE.forDimensions(DimensionalAnalysis.AREA);
    public static final Units VOLUME   = INSTANCE.forDimensions(DimensionalAnalysis.VOLUME);
    public static final Units TIME     = INSTANCE.forDimensions(DimensionalAnalysis.TIME);
    public static final Units VELOCITY = INSTANCE.forDimensions(DimensionalAnalysis.VELOCITY);
    public static final Units WEIGHT   = INSTANCE.forDimensions(DimensionalAnalysis.WEIGHT);
    public static final Units TEMPERATURE         = INSTANCE.forDimensions(DimensionalAnalysis.TEMPERATURE);
    public static final Units ELECTRIC_CURRENT    = INSTANCE.forDimensions(DimensionalAnalysis.ELECTRIC_CURRENT);
    public static final Units AMOUNT_OF_SUBSTANCE = INSTANCE.forDimensions(DimensionalAnalysis.AMOUNT_OF_SUBSTANCE);
    public static final Units LUMINOUS_INTENSITY  = INSTANCE.forDimensions(DimensionalAnalysis.LUMINOUS_INTENSITY);


    private SI() {

    }

    @Override
    @NonNull
    public SIUnit forDimension(Dimension dimension) {
        return SIUnit.valueOf(dimension);
    }

    @Override
    @NonNull
    public Units forDimensions(DimensionalAnalysis dimensions) {
        if (FORCE.equals(dimensions)) {
            return N;
        }
        if (ENERGY.equals(dimensions)) {
            return J;
        }
        if (FREQUENCY.equals(dimensions)) {
            return Hz;
        }
        if (PRESSURE.equals(dimensions)) {
            return Pa;
        }
        return SystemOfMeasurements.super.forDimensions(dimensions);
    }



    public enum BinaryPrefixes implements Prefix {
        KiB(1024),
        MiB(1024 * KiB.factor),
        GiB(1024 * MiB.factor),
        TiB(1024 * GiB.factor),
        PiB(1024 * TiB.factor),
        EiB(1024 * PiB.factor),
        ZiB(1024 * EiB.factor),
        YiB(1024 * ZiB.factor),
        ;

        private final long factor;

        BinaryPrefixes(long factor) {
            this.factor = factor;
        }

        @Override
        public double getAsDouble() {
            return factor;
        }

        @Override
        public Optional<BinaryPrefixes> times(Prefix prefix) {
            if (prefix instanceof BinaryPrefixes) {
                return forFactor(factor * ((BinaryPrefixes) prefix).factor);
            }
            return Optional.empty();
        }

        @Override
        public Optional<BinaryPrefixes> dividedBy(Prefix prefix) {
            if (prefix instanceof BinaryPrefixes) {
                return forFactor(factor / ((BinaryPrefixes) prefix).factor);
            }
            return Optional.empty();
        }

        @Override
        public Optional<BinaryPrefixes> reciprocal() {
            return Optional.empty();
        }
        public static Optional<BinaryPrefixes> forFactor(long factor) {
              for (BinaryPrefixes p : BinaryPrefixes.values()) {
                  if (p.factor == factor) {
                      return Optional.of(p);
                  }
              }
              return Optional.empty();
        }
    }

    public enum Prefixes implements Prefix {
        y(-24),
        z(-21),
        a(-18),
        f(-15),
        p(-12),
        n(-9),
        μ(-6),
        m(-3),
        c(-2),
        d(-1),
        da(1),
        h(2),
        k(3),
        M(6),
        G(9),
        T(12),
        P(15),
        E(18),
        Z(21),
        Y(24);
        //        public static final BigInteger Z = BigInteger.valueOf(E).multiply(BigInteger.valueOf(1000));
        //public static final BigInteger Y = Z.multiply(BigInteger.valueOf(1000));

        final double doubleValue;

        @Getter
        final int pow;

        Prefixes(int pow) {
            this.pow = pow;
            doubleValue = Utils.pow10(pow);
        }

        @Override
        public double getAsDouble() {
            return doubleValue;
        }

        @Override
        public Optional<Prefixes> times(Prefix prefix) {
            if (prefix instanceof Prefixes) {
                return forPow(pow + ((Prefixes) prefix).pow);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Prefixes> dividedBy(Prefix prefix) {
            if (prefix instanceof Prefixes) {
                return forPow(pow - ((Prefixes) prefix).pow);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Prefixes> reciprocal() {
            return forPow(-1 * pow);
        }

        public static Optional<Prefixes> forPow(int pow) {
              for (Prefixes p : values()) {
                  if (p.pow == pow) {
                      return Optional.of(p);
                  }
              }
              return Optional.empty();
        }
    }

    public static final DerivedUnit N =
        new DerivedUnit("N", "Newton", of(kg, 1), of(m, 1), of(s, -2));
    public static final DerivedUnit Hz =
        new DerivedUnit("Hz", "Hertz", of(s, -1));
    public static final DerivedUnit Pa =
        new DerivedUnit("Pa", "Pascal", of(kg, 1), of(m, -1), of(s, -2));
    public static final DerivedUnit J =
        new DerivedUnit("J", "joule", of(kg, 1), of(m, 2), of(s, -2));

    public static final DerivedUnit min =
        new DerivedUnit(exactly(60), "min", "minute", of(s, 1));
    public static final DerivedUnit hours =
        new DerivedUnit(exactly(60 * 60), "h", "hour", of(s, 1));


    public static final DerivedUnit eV =
        new DerivedUnit("eV", "electron-volt", exactly(1.602176634E-19), J);

    public static final DerivedUnit AU = new DerivedUnit("AU", "Astronomical Unit", exactly(149597870700d), m);
    public static final DerivedUnit pc = new DerivedUnit("pc", "parsec", exactly(648000 / Math.PI), AU);
    public static final DerivedUnit ly = new DerivedUnit("ly", "light-year", exactly(9460730472580800d), m);
}
