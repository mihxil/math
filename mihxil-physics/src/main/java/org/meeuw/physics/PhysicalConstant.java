package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalConstant extends PhysicalNumber<PhysicalConstant> {

    public static final PhysicalConstant NA = new PhysicalConstant(6.02214076e23d, Units.DIMENSIONLESS.dividedBy(UnitsImpl.of(SIUnit.mol)), "Avogadro's number");
    public static final PhysicalConstant c  = new PhysicalConstant(299792458d, UnitsImpl.of(SIUnit.m).dividedBy(UnitsImpl.of(SIUnit.s)), "speed of light");


    public static final PhysicalConstant h  = new PhysicalConstant(662607015e-34, UnitsImpl.of(SI.J).times(UnitsImpl.of(SIUnit.s)), "Planck constant");

    @Getter
    private final String name;

    public PhysicalConstant(double value, Units units, String name) {
        this(new ImmutableUncertainNumber(value, UncertainNumber.EXACT), units, name);
    }
    public PhysicalConstant(ImmutableUncertainNumber wrapped, Units units, String name) {
        super(wrapped, units);
        this.name = name;
    }

    @Override
    public UncertainNumbers<PhysicalConstant> structure() {
        return new UncertainNumbers<>(new PhysicalConstant(0, units, "zero"), new PhysicalConstant(1, units, "one"));

    }

    @Override
    public int compareTo(Number o) {
        return 0;
    }

    @Override
    protected PhysicalConstant copy(ImmutableUncertainNumber wrapped, Units units) {
        return new PhysicalConstant(wrapped, units, name);
    }
}
