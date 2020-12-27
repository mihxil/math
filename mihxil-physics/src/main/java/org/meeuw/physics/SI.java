package org.meeuw.physics;

import static org.meeuw.physics.SIUnit.*;
import static org.meeuw.physics.UnitExponent.of;
import static org.meeuw.physics.UnitsImpl.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class SI {

    public static final Units DISTANCE = of(m);
    public static final Units LENGTH   = of(m);
    public static final Units AREA     = of(m, m);
    public static final Units VOLUME   = of(m, m, m);
    public static final Units TIME     = of(s);
    public static final Units SPEED    = DISTANCE.dividedBy(TIME);
    public static final Units WEIGHT   = of(kg);
    public static final Units TEMPERATURE         = of(K);
    public static final Units ELECTRIC_CURRENT    = of(A);
    public static final Units AMOUNT_OF_SUBSTANCE = of(mol);
    public static final Units LUMINOUS_INTENSITY  = of(cd);

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

    public static final DerivedUnit AU = new DerivedUnit("AU", "Astronomical Unit", 149597870700d, m);
    public static final DerivedUnit pc = new DerivedUnit("pc", "parsec", 648000 / Math.PI, AU);
    public static final DerivedUnit ly = new DerivedUnit("ly", "lightyear", 9460730472580800d, m);
}
