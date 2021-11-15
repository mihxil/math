package org.meeuw.physics;

import lombok.Getter;

import java.math.BigInteger;
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
        return UnitsImpl.si(exactly(1), dimensions);
    }


    public enum BinaryPrefix implements Prefix {

        none(BigInteger.ONE, ""),
        Ki(BigInteger.valueOf(1024)),
        Mi(Ki.factor.multiply(Ki.factor)),
        Gi(Ki.factor.multiply(Mi.factor)),
        Ti(Ki.factor.multiply(Gi.factor)),
        Pi(Ki.factor.multiply(Ti.factor)),
        Ei(Ki.factor.multiply(Pi.factor)),
        Zi(Ki.factor.multiply(Ei.factor)),
        Yi(Ki.factor.multiply(Zi.factor))
        ;

        private final BigInteger factor;
        private final String string;

        BinaryPrefix(BigInteger factor) {
            this.factor = factor;
            this.string = name();
        }
        BinaryPrefix(BigInteger factor, String string) {
            this.factor = factor;
            this.string = string;
        }

        @Override
        public double getAsDouble() {
            return factor.doubleValue();
        }

        @Override
        public Optional<BinaryPrefix> times(Prefix prefix) {
            if (prefix instanceof BinaryPrefix) {
                return forFactor(factor.multiply(((BinaryPrefix) prefix).factor));
            }
            return Optional.empty();
        }

        @Override
        public Optional<BinaryPrefix> dividedBy(Prefix prefix) {
            if (prefix instanceof BinaryPrefix) {
                return forFactor(factor.divide(((BinaryPrefix) prefix).factor));
            }
            return Optional.empty();
        }

        @Override
        public Optional<BinaryPrefix> reciprocal() {
            return Optional.empty();
        }

        @Override
        public Optional<? extends Prefix> inc() {
            return forFactor(factor.multiply(Ki.factor));
        }

        @Override
        public Optional<? extends Prefix> dec() {
            return forFactor(factor.multiply(Ki.factor));
        }

        @Override
        public String toString() {
            return string;
        }
        public static Optional<BinaryPrefix> forFactor(BigInteger factor) {
              for (BinaryPrefix p : BinaryPrefix.values()) {
                  if (p.factor.equals(factor)) {
                      return Optional.of(p);
                  }
              }
              return Optional.empty();
        }
    }

    public enum DecimalPrefix implements Prefix {
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
        none(0, ""),
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

        final String string;

        DecimalPrefix(int pow, String string) {
            this.pow = pow;
            doubleValue = Utils.pow10(pow);
            this.string = string;
        }

        DecimalPrefix(int pow) {
            this.pow = pow;
            doubleValue = Utils.pow10(pow);
            this.string = name();
        }


        @Override
        public double getAsDouble() {
            return doubleValue;
        }

        @Override
        public Optional<DecimalPrefix> times(Prefix prefix) {
            if (prefix instanceof DecimalPrefix) {
                return forPow(pow + ((DecimalPrefix) prefix).pow);
            }
            return Optional.empty();
        }

        @Override
        public Optional<DecimalPrefix> dividedBy(Prefix prefix) {
            if (prefix instanceof DecimalPrefix) {
                return forPow(pow - ((DecimalPrefix) prefix).pow);
            }
            return Optional.empty();
        }

        @Override
        public Optional<DecimalPrefix> reciprocal() {
            return forPow(-1 * pow);
        }

        @Override
        public Optional<? extends Prefix> inc() {
            return forPow(pow + 3);
        }

        @Override
        public Optional<? extends Prefix> dec() {
            return forPow(pow - 3);
        }

        @Override
        public String toString() {
            return string;
        }

        public static Optional<DecimalPrefix> forPow(int pow) {
              for (DecimalPrefix p : values()) {
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
    public static final DerivedUnit hour =
        new DerivedUnit(exactly(60 * 60), "h", "hour", of(s, 1));


    public static final DerivedUnit eV =
        new DerivedUnit("eV", "electron-volt", exactly(1.602176634E-19), J);

    public static final DerivedUnit AU = new DerivedUnit("AU", "Astronomical Unit", exactly(149597870700d), m);
    public static final DerivedUnit pc = new DerivedUnit("pc", "parsec", exactly(648000 / Math.PI), AU);
    public static final DerivedUnit ly = new DerivedUnit("ly", "light-year", exactly(9460730472580800d), m);
}
