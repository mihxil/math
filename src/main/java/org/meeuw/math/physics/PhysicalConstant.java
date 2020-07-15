package org.meeuw.math.physics;

import lombok.Getter;

import org.meeuw.math.Utils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalConstant extends PhysicalNumber {

    public static final PhysicalConstant NA = new PhysicalConstant(6.02214076e23d, Units.DIMENSIONLESS.dividedBy(UnitsImpl.of(SIUnit.mol)), "Avogadro's number");
    public static final PhysicalConstant c  = new PhysicalConstant(299792458d, UnitsImpl.of(SIUnit.m).dividedBy(UnitsImpl.of(SIUnit.s)), "speed of light");


    public static final PhysicalConstant h  = new PhysicalConstant(662607015e-34, UnitsImpl.of(SI.J).times(UnitsImpl.of(SIUnit.s)), "Planck constant");
    private final double value;

    private final double uncertainty;

    @Getter
    private final String name;

    public PhysicalConstant(double value, Units units, String name) {
        super(units);
        this.value = value;
        this.name = name;
        this.uncertainty = 0;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public double getUncertainty() {
        return uncertainty;
    }

    @Override
    public PhysicalNumber times(double multiplication) {
        return new PhysicalConstant(value * multiplication, units, multiplication + Utils.TIMES + name);
    }

    @Override
    public PhysicalNumber plus(PhysicalNumber m) {
        return new PhysicalConstant(value + m.doubleValue(),  Units.forAddition(units, m.units), name + "+" + m);
    }

    @Override
    public PhysicalNumber times(PhysicalNumber m) {
        return new PhysicalConstant(value * m.doubleValue(), Units.forMultiplication(units, m.units), name + Utils.TIMES + m);

    }

    @Override
    public PhysicalNumber pow(int e) {
        return null;

    }
}
