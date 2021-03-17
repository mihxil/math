package org.meeuw.physics;

import lombok.Getter;

import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.text.TextUtils.subscript;

/**
 * <p>A physical constant is a {@link PhysicalNumber} but to it are associated a {@code name} and a {@code symbol}
 * </p>
 * <p>
 * Some physical constants are principally error free. E.g. the value of {@link #c} has an uncertainty of {@link UncertainReal#EXACT}, because {@code c} has indeed an exact value. Any remaining uncertainty is in the <em>units</em>, because {@code c} is used to <em>define</em> them.
 * </p>
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalConstant extends PhysicalNumber {

    public static final PhysicalConstant NA = new PhysicalConstant("N" + subscript("A"), 6.02214076e23d, Units.DIMENSIONLESS.dividedBy(Units.of(SIUnit.mol)), "Avogadro's number");
    public static final PhysicalConstant c  = new PhysicalConstant("c", 299792458d, Units.of(SIUnit.m).dividedBy(Units.of(SIUnit.s)), "speed of light");


    public static final PhysicalConstant h  = new PhysicalConstant("h", 6.62607015e-34, Units.of(SI.J).times(Units.of(SIUnit.s)), "Planck constant");

    public static final PhysicalConstant hbar  = new PhysicalConstant("\u0127", 1.0545718176461563912624280033022807447228263300204131224219234705984359127347390624985306286124570001989184744174155120232087219301212030916619114529221059634766659521109532072460048990299368330557758e-34,  Units.of(SI.J).times(Units.of(SIUnit.s)), "Reduced Planck constant");

    public static final PhysicalConstant G  = new PhysicalConstant("G",
        new UncertainDoubleElement(6.6743e-11d, 0.00015e-11d),
        Units.of(SIUnit.m, SIUnit.m, SIUnit.m)
            .dividedBy(Units.of(SIUnit.kg, SIUnit.s, SIUnit.s)), "Gravitational Constant");


    @Getter
    private final String name;

    @Getter
    private final String symbol;

    public PhysicalConstant(String symbol, double value, Units units, String name) {
        this(symbol, new UncertainDoubleElement(value, UncertainDouble.EXACT), units, name);
    }

    public PhysicalConstant(String symbol, UncertainReal wrapped, Units units, String name) {
        super(wrapped, units);
        this.name = name;
        this.symbol = symbol;
    }


    @Override
    protected PhysicalConstant copy(UncertainReal wrapped, Units units) {
        return new PhysicalConstant(symbol, wrapped, units, name);
    }

}
