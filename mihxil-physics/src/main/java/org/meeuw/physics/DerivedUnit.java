package org.meeuw.physics;

import lombok.*;

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

    final int[] exponents;

    @Getter
    @With
    final Prefix prefix;

    @Getter
    @With
    final String name;

    @Getter
    @With
    final String description;

    public DerivedUnit(
        UncertainReal SIFactor,
        String name,
        String description,
        UnitExponent... siExponents) {
        this(SIFactor, null, null, name, description, Arrays.asList(siExponents));
    }


    private DerivedUnit(
        UncertainReal SIFactor,
        int[] exponents,
        Prefix prefix,
        String name,
        String description) {
        this(SIFactor, exponents, prefix, name, description, null);
    }


    @lombok.Builder
    private DerivedUnit(
        UncertainReal siFactor,
        int[] exponents,
        Prefix prefix,
        String name,
        String description,
        List<UnitExponent> siExponents
        ) {
        this.exponents =  new int[SIUnit.values().length];
        if (exponents != null) {
            System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
        }
        if (siExponents != null) {
            for (UnitExponent f : siExponents) {
                int[] dimensions = f.getDimensions().getExponents();
                for (int d = 0; d < dimensions.length; d++) {
                    this.exponents[d] += dimensions[d];
                };
            }
        }
        this.name = name;
        this.description = description;
        this.SIFactor = siFactor;
        this.prefix = prefix == null ? SI.DecimalPrefix.none : prefix;
    }


    public DerivedUnit(Units units, String name, String description) {
        this.exponents = new int[SIUnit.values().length];
        System.arraycopy(units.getDimensions().getExponents(), 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = units.getSIFactor();
        this.prefix = SI.DecimalPrefix.none;
    }


    public DerivedUnit(String name, String description, UncertainReal siFactor, DerivedUnit derivedUnit) {
        this.exponents = new int[SIUnit.values().length];
        System.arraycopy(derivedUnit.exponents, 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = derivedUnit.SIFactor.times(siFactor);
        this.prefix = derivedUnit.prefix();
    }

    public DerivedUnit(String name, String description, UncertainReal siFactor, SIUnit siUnit) {
        this.exponents = new int[SIUnit.values().length];
        this.exponents[siUnit.ordinal()] = 1;
        this.name = name;
        this.description = description;
        this.prefix = SI.DecimalPrefix.none;
        this.SIFactor = siFactor;
    }

    public DerivedUnit(Prefix prefix, DerivedUnit unit) {
        this.exponents = new int[SIUnit.values().length];
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
    public DimensionalAnalysis getDimensions() {
        return new DimensionalAnalysis(this.exponents);
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

        UncertainReal factor = SIFactor.times(multiplier.getSIFactor());

        if (Arrays.stream(exponents).allMatch(i -> i == 0) && factor.isOne()) {
            return DerivedUnit.DIMENSIONLESS;
        }

        return DerivedUnit.builder()
            .siFactor(factor)
            .name(name + TextUtils.TIMES + multiplier)
            .exponents(exponents)
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
        if (Arrays.stream(exponents).allMatch(i -> i == 0) && reciprocalFactor.isOne()) {
            return DerivedUnit.DIMENSIONLESS;
        }
        return DerivedUnit.builder()
            .siFactor(reciprocalFactor)
            .prefix(p)
            .name("(" + name + ")" + TextUtils.superscript(-1))
            .exponents(exponents)
            .build();
    }


    @SuppressWarnings("EqualsDoesntCheckParameterClass")
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
