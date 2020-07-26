package org.meeuw.physics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DerivedUnit implements Unit {

    @Getter
    @EqualsAndHashCode.Include
    final double siFactor;
    @EqualsAndHashCode.Include
    final int[] exponents = new int[SIUnit.values().length];

    @EqualsAndHashCode.Include
    final Prefix prefix;

    final Dimensions dimensions;

    @EqualsAndHashCode.Include
    final String name;

    @Getter
    final String description;

    public DerivedUnit(double siFactor, String name, String description, UnitExponent... siExponents) {
        for (UnitExponent f : siExponents) {
            this.exponents[((SIUnit) f.unit).ordinal()] = f.exponent;
        }
        this.dimensions = new Dimensions(exponents);
        this.name = name;
        this.description = description;
        this.siFactor = siFactor;
        this.prefix = Prefix.NONE;
    }


    public DerivedUnit(String name, String description, double siFactor, DerivedUnit derivedUnit) {
        for (int i = 0; i < this.exponents.length; i++) {
            this.exponents[i] = derivedUnit.exponents[i];
        }
        this.dimensions = derivedUnit.dimensions;
        this.name = name;
        this.description = description;
        this.siFactor = derivedUnit.siFactor * siFactor;
        this.prefix = derivedUnit.prefix();
    }
    public DerivedUnit(Prefix prefix, DerivedUnit unit) {
        System.arraycopy(unit.exponents, 0, this.exponents, 0, unit.exponents.length);
        this.dimensions = new Dimensions(exponents);
        this.name = unit.name;
        this.description = prefix.toString() + "(" + unit.description + ")";
        this.siFactor = unit.siFactor * prefix.getAsDouble();
        this.prefix = prefix;
    }
    public DerivedUnit(Prefix prefix, SIUnit unit) {
        this(prefix, new DerivedUnit(1d, unit.name(), unit.getDescription(), new UnitExponent(unit, 1)));
    }

    public DerivedUnit(String name, String description, UnitExponent... factors) {
        this(1, name, description,  factors);
    }

    @Override
    public Dimensions getDimensions() {
        return dimensions;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return prefix() + name();
    }
    //@Override
    public Prefix prefix() {
        return prefix;
    }

}
