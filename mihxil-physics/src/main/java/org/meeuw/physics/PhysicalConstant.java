package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalConstant extends PhysicalNumber {

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
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    protected PhysicalConstant copy(ImmutableUncertainNumber wrapped, Units units) {
        return new PhysicalConstant(wrapped, units, name);
    }
}
