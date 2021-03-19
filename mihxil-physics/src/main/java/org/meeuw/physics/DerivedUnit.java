package org.meeuw.physics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DerivedUnit implements Unit {

    @Getter
    @EqualsAndHashCode.Include
    final UncertainReal SIFactor;

    @EqualsAndHashCode.Include
    final int[] exponents = new int[SIUnit.values().length];

    @EqualsAndHashCode.Include
    final Prefix prefix;

    final Dimensions dimensions;

    @EqualsAndHashCode.Include
    @Getter
    final String name;

    @Getter
    final String description;

    public DerivedUnit(UncertainReal siFactor, String name, String description, UnitExponent... siExponents) {
        for (UnitExponent f : siExponents) {
            this.exponents[((SIUnit) f.unit).ordinal()] = f.exponent;
        }
        this.dimensions = new Dimensions(exponents);
        this.name = name;
        this.description = description;
        this.SIFactor = siFactor;
        this.prefix = Prefix.NONE;
    }

    @lombok.Builder
    public DerivedUnit(UncertainReal siFactor,
                       String name,
                       String description,
                       List<UnitExponent> siExponents) {
        this(siFactor, name, description, siExponents.toArray(new IntFunction<UnitExponent[]>() {
            @Override
            public UnitExponent[] apply(int value) {
                return new UnitExponent[value];

            }
        }));
    }


    public DerivedUnit(Units units, String name, String description) {
        System.arraycopy(units.getDimensions().getExponents(), 0, this.exponents, 0, this.exponents.length);
        this.dimensions = new Dimensions(exponents);
        this.name = name;
        this.description = description;
        this.SIFactor = units.getSIFactor();
        this.prefix = Prefix.NONE;
    }


    public DerivedUnit(String name, String description, UncertainReal siFactor, DerivedUnit derivedUnit) {
        System.arraycopy(derivedUnit.exponents, 0, this.exponents, 0, this.exponents.length);
        this.dimensions = derivedUnit.dimensions;
        this.name = name;
        this.description = description;
        this.SIFactor = derivedUnit.SIFactor.times(siFactor);
        this.prefix = derivedUnit.prefix();
    }

    public DerivedUnit(String name, String description, UncertainReal siFactor, SIUnit siUnit) {
        this.exponents[siUnit.ordinal()] = 1;
        this.dimensions = siUnit.getDimensions();
        this.name = name;
        this.description = description;
        this.prefix = Prefix.NONE;
        this.SIFactor = siFactor;
    }

    public DerivedUnit(Prefix prefix, DerivedUnit unit) {
        System.arraycopy(unit.exponents, 0, this.exponents, 0, unit.exponents.length);
        this.dimensions = new Dimensions(exponents);
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
        return dimensions;
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
        // TODO
        return DerivedUnit.builder()
            .siFactor(SIFactor.times(multiplier.getSIFactor()))
            .name(name + TextUtils.TIMES + multiplier)
            .siExponents(new ArrayList<>())
            .build();
    }

    @Override
    public Units reciprocal() {
        // TODO
        return DerivedUnit.builder()
            .siFactor(SIFactor.reciprocal())
            .name(name + TextUtils.superscript(-1))
            .siExponents(new ArrayList<>())
            .build();
    }

    public static class Builder {



    }
}
