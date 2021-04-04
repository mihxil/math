package org.meeuw.physics;

import lombok.Getter;

import java.util.*;

import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DerivedUnit implements Unit {

    @Getter
    final UncertainReal SIFactor;

    final int[] exponents = new int[SIUnit.values().length];
    @Getter
    final Prefix prefix;

    @Getter
    final String name;

    @Getter
    final String description;

    public DerivedUnit(
        UncertainReal siFactor,
        String name,
        String description,
        UnitExponent... siExponents) {
        this(siFactor, name, description, Arrays.asList(siExponents), null, null);
    }

    @lombok.Builder
    private DerivedUnit(
        UncertainReal siFactor,
        String name,
        String description,
        List<UnitExponent> siExponents,
        int[] exponents,
        Prefix prefix
        ) {
        if (exponents != null) {
            System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
        }
        if (siExponents != null) {
            for (UnitExponent f : siExponents) {
                this.exponents[((SIUnit) f.unit).ordinal()] += f.exponent;
            }
        }
        this.name = name;
        this.description = description;
        this.SIFactor = siFactor;
        this.prefix = prefix == null ? Prefix.NONE : prefix;
    }


    public DerivedUnit(Units units, String name, String description) {
        System.arraycopy(units.getDimensions().getExponents(), 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = units.getSIFactor();
        this.prefix = Prefix.NONE;
    }


    public DerivedUnit(String name, String description, UncertainReal siFactor, DerivedUnit derivedUnit) {
        System.arraycopy(derivedUnit.exponents, 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = derivedUnit.SIFactor.times(siFactor);
        this.prefix = derivedUnit.prefix();
    }

    public DerivedUnit(String name, String description, UncertainReal siFactor, SIUnit siUnit) {
        this.exponents[siUnit.ordinal()] = 1;
        this.name = name;
        this.description = description;
        this.prefix = Prefix.NONE;
        this.SIFactor = siFactor;
    }

    public DerivedUnit(Prefix prefix, DerivedUnit unit) {
        System.arraycopy(unit.exponents, 0, this.exponents, 0, unit.exponents.length);
        this.name = unit.name;
        this.description = prefix.toString() + "(" + unit.description + ")";
        this.SIFactor = unit.SIFactor.times(prefix.getAsDouble());
        this.prefix = prefix;
    }
    public DerivedUnit(Prefix prefix, SIUnit unit) {
        this(prefix, new DerivedUnit(INSTANCE.one(), unit.name(), unit.getDescription(), new UnitExponent(unit, 1)));
    }

    public DerivedUnit(String name, String description, UnitExponent... factors) {
        this(INSTANCE.one(), name, description,  factors);
    }

    @Override
    public Dimensions getDimensions() {
        return new Dimensions(this.exponents);
    }

    @Override
    public String name() {
        return name;
    }

    //@Override
    public Prefix prefix() {
        return prefix;
    }

    public PhysicalConstant toSI() {
        return new PhysicalConstant(name, SIFactor,
            SIUnit.toUnits(exponents),description);
    }

    @Override
    public String toString() {
        return prefix() + name();
    }

    @Override
    public Units times(Units multiplier) {
        if (multiplier.isOne()){
            return this;
        }
        int[] exponents = getDimensions().times(multiplier.getDimensions()).getExponents();
        Prefix p = prefix.times(multiplier.getPrefix()).orElse(null);

        UncertainReal factor = SIFactor.times(multiplier.getSIFactor());
        if (p == null) {
            factor = factor.times(prefix.getAsDouble() * multiplier.getPrefix().getAsDouble());
        }

        if (Arrays.stream(exponents).allMatch(i -> i == 0) && factor.isOne()) {
            return DerivedUnit.DIMENSIONLESS;
        }

        return DerivedUnit.builder()
            .siFactor(factor)
            .name("(" + name + ") " + TextUtils.TIMES + "(" + multiplier + ")")
            .exponents(exponents)
            .prefix(p)
            .build();
    }

    @Override
    public Units reciprocal() {
        int[] exponents = new int[this.exponents.length];
        for (int i = 0; i < this.exponents.length; i++) {
            exponents[i] = -1 * this.exponents[i];
        }
        Prefix p = prefix.reciprocal().orElse(null);
        UncertainReal reciprocalFactor = SIFactor.reciprocal();
        if (p == null) {
            reciprocalFactor = reciprocalFactor.dividedBy(prefix.getAsDouble());
        }
        return DerivedUnit.builder()
            .siFactor(reciprocalFactor)
            .prefix(p)
            .name("(" + name + ")" + TextUtils.superscript(-1))
            .exponents(exponents)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        return Units.equals(this, o);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Builder {



    }
}
