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
    final double SIFactor;

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
        this.SIFactor = siFactor;
        this.prefix = Prefix.NONE;
    }


    public DerivedUnit(String name, String description, double siFactor, DerivedUnit derivedUnit) {
        System.arraycopy(derivedUnit.exponents, 0, this.exponents, 0, this.exponents.length);
        this.dimensions = derivedUnit.dimensions;
        this.name = name;
        this.description = description;
        this.SIFactor = derivedUnit.SIFactor * siFactor;
        this.prefix = derivedUnit.prefix();
    }

    public DerivedUnit(String name, String description, double siFactor, SIUnit siUnit) {
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
        this.SIFactor = unit.SIFactor * prefix.getAsDouble();
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

    public PhysicalConstant toSI() {
        return new PhysicalConstant(name, SIFactor,
            SIUnit.toUnits(exponents),description);
    }

}
