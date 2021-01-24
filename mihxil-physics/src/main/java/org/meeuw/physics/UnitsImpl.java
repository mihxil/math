package org.meeuw.physics;

import lombok.Getter;

import java.util.*;

import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.uncertainnumbers.AbstractUncertainDouble;

/**
 * Represents the units of a {@link AbstractUncertainDouble}.
 *
 * Basicly keeps track of a integer power for each of the basic SI units.
 *
 * @author Michiel Meeuwissen
 */
public class UnitsImpl implements Units  {

    @Getter
    private final double SIFactor;
    private final UnitExponent[] exponents;

    public UnitsImpl(double siFactor, Unit... units) {
        this(siFactor, Unit.toArray(units));
    }

    public UnitsImpl(double siFactor, UnitExponent... units) {
        this.exponents = units;
        this.SIFactor = siFactor;
    }

    public static UnitsImpl of(double siFactor, Unit... units) {
        return new UnitsImpl(siFactor, units);
    }

    public static UnitsImpl of(Unit... units) {
        double factor = 1;
        for (Unit u : units) {
            factor *= u.getSIFactor();
        }
        return new UnitsImpl(factor, units);
    }

    @Override
    public UnitsGroup getStructure() {
        return UnitsGroup.INSTANCE;
    }

    @Override
    public Units reciprocal() {
        return pow(-1);
    }

    @Override
    public Units times(Units multiplier) {
        List<UnitExponent> base = new ArrayList<>(Arrays.asList(exponents));
        OUTER:
        for (UnitExponent u : multiplier) {
            for (int i = 0 ; i < base.size(); i++) {
                UnitExponent n = base.get(i);
                if (n.unit.equals(u.unit)) {
                    base.set(i, n.times(u));
                    if (base.get(i).exponent == 0) {
                        base.remove(i);
                    }
                    continue OUTER;
                }
            }
            if (u.exponent != 0) {
                base.add(u);
            }
        }
        return new UnitsImpl(SIFactor * multiplier.getSIFactor(), base.toArray(new UnitExponent[0]));
    }

    @Override
    public Units pow(int exponent) {
        List<UnitExponent> base = new ArrayList<>(Arrays.asList(exponents));
        for (int i = 0 ; i < base.size(); i++) {
            UnitExponent n = base.get(i);
            base.set(i, n.pow(exponent));
            if (base.get(i).exponent == 0) {
                base.remove(i--);
            }
        }
        return new UnitsImpl(Math.pow(SIFactor, exponent), base.toArray(new UnitExponent[0]));
    }

    @Override
    public Dimensions dimensions() {
        int[] dimexponents =  new int[Dimension.NUMBER];
        for (UnitExponent u : exponents) {
            int[] uexponents = u.getDimensions().getExponents();
            for (int i = 0; i < Dimension.NUMBER; i++) {
                dimexponents[i] += uexponents[i];
            }
        }
        return new Dimensions(dimexponents);
    }

    @Override
    public PhysicalConstant zero() {
        return new PhysicalConstant("0", 0, this, "zero " + toString());
    }



    @Override
    public Iterator<UnitExponent> iterator() {
        return Arrays.stream(exponents).iterator();
    }

    public UnitExponent[] getCanonicalExponents() {
        UnitExponent[] copy = Arrays.copyOf(exponents, exponents.length);
        Arrays.sort(copy);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitsImpl that = (UnitsImpl) o;
        return Arrays.equals(getCanonicalExponents(), that.getCanonicalExponents());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (UnitExponent e :exponents) {
            if (e.exponent != 0) {
                if (builder.length() > 0) {
                    builder.append(UncertainDoubleFormat.TIMES);
                }

                builder.append(e.toString());
            }
        }
        return builder.toString();
    }

}
