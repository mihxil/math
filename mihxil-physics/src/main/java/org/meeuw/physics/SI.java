/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.physics;

import lombok.Getter;

import java.math.BigInteger;
import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Utils;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.Quantity.*;
import static org.meeuw.physics.SIUnit.*;
import static org.meeuw.physics.UnitExponent.of;

/**
 * The International System of Units
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class SI implements SystemOfMeasurements {

    public static final SI INSTANCE = new SI();

    private SI() {

    }

    @Override
    @NonNull
    public SIUnit forDimension(Dimension dimension) {
        return SIUnit.valueOf(dimension);
    }

    @Override
    public  List<BaseUnit> getBaseUnits() {
        return Arrays.asList(SIUnit.values());
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

    public static final Units mPerS = m.per(s).withQuantity(VELOCITY);

    public static final Units km = m.withPrefix(DecimalPrefix.k).withQuantity(DISTANCE);


    public static final DerivedUnit litre = new DerivedUnit(
        "l", "litre",
        of(m.withPrefix(SI.DecimalPrefix.d),3)).withQuantity(VOLUME);

    public static final DerivedUnit N =
        new DerivedUnit("N", "Newton", of(kg, 1), of(m, 1), of(s, -2))
            .withQuantity(FORCE);

    public static final DerivedUnit g =
        new DerivedUnit(exactly(0.001), "g", "gram", of(kg, 1))
            .withQuantity(MASS);
    public static final DerivedUnit Hz =
        new DerivedUnit("Hz", "Hertz", of(s, -1)).withQuantity(Quantity.FREQUENCY);
    public static final DerivedUnit Pa =
        new DerivedUnit("Pa", "Pascal", of(kg, 1), of(m, -1), of(s, -2))
            .withQuantity(PRESSURE);

    /**
     * The SI unit of energy 'Joule'
     */
    public static final DerivedUnit J =
        new DerivedUnit("J", "joule", of(kg, 1), of(m, 2), of(s, -2))
            .withQuantity(ENERGY);

    public static final DerivedUnit min =
        new DerivedUnit(exactly(60), "min", "minute", of(s, 1))
            .withQuantity(TIME);
    public static final DerivedUnit hour =
        new DerivedUnit(exactly(60 * 60), "h", "hour", of(s, 1))
            .withQuantity(TIME);


    public static final DerivedUnit eV =
        new DerivedUnit("eV", "electron-volt", exactly(1.602176634E-19), J)
            .withQuantity(ENERGY);

    public static final DerivedUnit AU = new DerivedUnit("AU", "Astronomical Unit", exactly(149597870700d), m)
        .withQuantity(DISTANCE);
    public static final DerivedUnit pc = new DerivedUnit("pc", "parsec", exactly(648000 / Math.PI), AU)
        .withQuantity(DISTANCE);
    public static final DerivedUnit ly = new DerivedUnit("ly", "light-year", exactly(9460730472580800d), m)
        .withQuantity(DISTANCE);

    public static final DerivedUnit Da = new DerivedUnit("Da", "dalton",
        UncertainDoubleElement.of(
            1.660539040e-27,
            0.000000020e-27), kg)
        .withQuantity(MASS);


    public static final DerivedUnit bit = new DerivedUnit("bit", "binary digit").withQuantity(INFORMATION);

    /**
     * 8 bits. Often also simply called 'byte' (but that traditionally was architecture dependent).
     */
    public static final DerivedUnit octet = bit.times(8).withName("Byte").withDescription("8 bit octet");



}
