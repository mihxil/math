package org.meeuw.physics;

import lombok.Getter;

import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum SIUnit implements Unit {

    m(L, "meter"),
    kg(M, "kilogram"),
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
    private final Dimensions dimensions;

    @Getter
    private final String description;

    SIUnit(Dimension dimension, String description) {
        this.dimension = dimension;
        this.description = description;
        this.dimensions = getDimensions(ordinal());
    }

    private static  Dimensions getDimensions(int ord) {
        int[] exponents = new int[7];
        exponents[ord] = 1;
        return new Dimensions(exponents);
    }

    public Prefix prefix() {
        return Prefix.NONE;
    }

    public static Units toUnits(int[] exponents) {
        UnitExponent[] unitExponents =  new UnitExponent[Dimension.values().length];
        for (int i = 0; i < exponents.length; i++) {
            unitExponents[i] = UnitExponent.of(values()[i], exponents[i]);
        }
        return new UnitsImpl(1, unitExponents);
    }

    @Override
    public double getSIFactor() {
        return 1;
    }
}
