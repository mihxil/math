package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

import static org.meeuw.math.text.TextUtils.subscript;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalConstant extends PhysicalNumber {

    public static final PhysicalConstant NA = new PhysicalConstant("N" + subscript("A"), 6.02214076e23d, Units.DIMENSIONLESS.dividedBy(UnitsImpl.of(SIUnit.mol)), "Avogadro's number");
    public static final PhysicalConstant c  = new PhysicalConstant("c", 299792458d, UnitsImpl.of(SIUnit.m).dividedBy(UnitsImpl.of(SIUnit.s)), "speed of light");


    public static final PhysicalConstant h  = new PhysicalConstant("h", 6.62607015e-34, UnitsImpl.of(SI.J).times(UnitsImpl.of(SIUnit.s)), "Planck constant");

    public static final PhysicalConstant hbar  = new PhysicalConstant("\u0127", 1.0545718176461563912624280033022807447228263300204131224219234705984359127347390624985306286124570001989184744174155120232087219301212030916619114529221059634766659521109532072460048990299368330557758e-34,  UnitsImpl.of(SI.J).times(UnitsImpl.of(SIUnit.s)), "Reduced Planck constant");

    public static final PhysicalConstant G  = new PhysicalConstant("G",
        new ImmutableUncertainNumber(6.6743e-11d, 0.00015e-11d), UnitsImpl.of(SIUnit.m, SIUnit.m, SIUnit.m).dividedBy(UnitsImpl.of(SIUnit.kg, SIUnit.s, SIUnit.s)), "Gravitational Constant");


    @Getter
    private final String name;

    @Getter
    private final String symbol;

    public PhysicalConstant(String symbol, double value, Units units, String name) {
        this(symbol, new ImmutableUncertainNumber(value, UncertainNumber.EXACT), units, name);
    }

    public PhysicalConstant(String symbol, UncertainNumber wrapped, Units units, String name) {
        super(wrapped, units);
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    protected PhysicalConstant copy(UncertainNumber wrapped, Units units) {
        return new PhysicalConstant(symbol, wrapped, units, name);
    }


}
