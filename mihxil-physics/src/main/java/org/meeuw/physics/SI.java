package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.Utils;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exact;
import static org.meeuw.physics.SIUnit.*;
import static org.meeuw.physics.UnitExponent.of;
import static org.meeuw.physics.Units.of;

/**
 * The International System of Units
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class SI implements SystemOfMeasurements {

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

    @Override
    public SIUnit forDimension(Dimension dimension) {
        return SIUnit.valueOf(dimension);
    }

    @Override
    public Units forDimensions(Dimensions dimensions) {
        // TODO
        return null;

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
        new DerivedUnit(exact(60), "min", "minute", of(s, 1));
    public static final DerivedUnit hours =
        new DerivedUnit(exact(60 * 60), "h", "hour", of(s, 1));


    public static final DerivedUnit eV =
        new DerivedUnit("eV", "electronvolt", exact(1.602176634E-19), J);

    public static final DerivedUnit AU = new DerivedUnit("AU", "Astronomical Unit", exact(149597870700d), m);
    public static final DerivedUnit pc = new DerivedUnit("pc", "parsec", exact(648000 / Math.PI), AU);
    public static final DerivedUnit ly = new DerivedUnit("ly", "lightyear", exact(9460730472580800d), m);
}
