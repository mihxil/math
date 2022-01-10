package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum SIUnit implements BaseUnit {

    m(L, "meter"),
    kg(M, "kilogram") {
        @Override
        public Unit withPrefix(Prefix prefix) {
            if (prefix == SI.DecimalPrefix.k) {
                return this;
            } else {
                return SI.g.withPrefix(prefix);
            }
        }
    },
    s(T, "second"),
    A(I, "ampere"),
    K(TH, "kelvin"),
    mol(N, "mole"),
    cd(J, "candela");

    static final int NUMBER = values().length;

    static {
        assert NUMBER == Dimension.NUMBER;
        for (SIUnit v : values()){
            assert v.ordinal() == v.dimension.ordinal();
        }
    }

    @Getter
    private final Dimension dimension;

    @Getter
    private final String description;

    SIUnit(Dimension dimension, String description) {
        this.dimension = dimension;
        this.description = description;
    }

    @Override
    public DimensionalAnalysis getDimensions() {
        return getDimensions(ordinal());
    }

    @Override
    public SystemOfMeasurements getSystem() {
        return SI.INSTANCE;
    }




    private static DimensionalAnalysis getDimensions(int ord) {
        int[] exponents = new int[7];
        exponents[ord] = 1;
        return new DimensionalAnalysis(exponents);
    }

    public static Units toUnits(int[] exponents) {
        UnitExponent[] unitExponents =  new UnitExponent[Dimension.values().length];
        for (int i = 0; i < exponents.length; i++) {
            unitExponents[i] = UnitExponent.of(values()[i], exponents[i]);
        }
        return new CompositeUnits(UncertainRealField.INSTANCE.one(), unitExponents);
    }

    @Override
    public UncertainReal getSIFactor() {
        return UncertainRealField.INSTANCE.one();
    }

    public static SIUnit valueOf(Dimension dimension) {
        for (SIUnit si :  SIUnit.values()) {
            if (si.dimension == dimension) {
                return si;
            }
        }
        throw new IllegalArgumentException();
    }


}
