package org.meeuw.math.physics;

import static org.meeuw.math.physics.SIUnit.*;
import static org.meeuw.math.physics.UnitExponent.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class SI {
    public enum Prefixes implements Prefix {

        KiB(1024),
        MiB(1024 * KiB.factor),
        GiB(1024 * MiB.factor),

        k(1000),
        M(1000 * k.factor),
        G(1000 * M.factor),
        T(1000 * G.factor),
        P(1000 * T.factor),
        E(1000 * P.factor);
        //        public static final BigInteger Z = BigInteger.valueOf(E).multiply(BigInteger.valueOf(1000));
        //public static final BigInteger Y = Z.multiply(BigInteger.valueOf(1000));

        private final long factor;

        Prefixes(long factor) {
            this.factor = factor;
        }

        @Override
        public double getAsDouble() {
            return factor;
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
        new DerivedUnit(60, "min", "minute", of(s, 1));
    public static final DerivedUnit hours =
        new DerivedUnit(60 * 60, "h", "hour", of(s, 1));


    public static final DerivedUnit eV =
        new DerivedUnit("eV", "electonvolt", 1.602176634E-19, J);
}
