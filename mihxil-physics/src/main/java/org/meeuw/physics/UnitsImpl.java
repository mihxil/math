package org.meeuw.physics;

import java.util.*;

import org.meeuw.math.text.UncertainNumberFormat;
import org.meeuw.math.uncertainnumbers.AbstractUncertainNumber;

/**
 * Represents the units of a {@link AbstractUncertainNumber}.
 *
 * Basicly keeps track of a integer power for each of the basic SI units.
 *
 * @author Michiel Meeuwissen
 */
public class UnitsImpl implements Units  {

    private final UnitExponent[] exponents;

    public UnitsImpl(Unit... units) {
        this(Unit.toArray(units));
    }

    public UnitsImpl(UnitExponent... units) {
        this.exponents = units;
    }

    public static UnitsImpl of(Unit... units) {
        return new UnitsImpl(units);
    }

    @Override
    public UnitsGroup getStructure() {
        return UnitsGroup.INSTANCE;
    }

    @Override
    public Units self() {
        return this;
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
        return new UnitsImpl(base.toArray(new UnitExponent[0]));
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
        return new UnitsImpl(base.toArray(new UnitExponent[0]));
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (UnitExponent e :exponents) {
            if (e.exponent != 0) {
                if (builder.length() > 0) {
                    builder.append(UncertainNumberFormat.TIMES);
                }

                builder.append(e.toString());
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitsImpl that = (UnitsImpl) o;
        return Arrays.equals(getCanonicalExponents(), that.getCanonicalExponents());
    }

    public UnitExponent[] getCanonicalExponents() {
        UnitExponent[] copy = Arrays.copyOf(exponents, exponents.length);
        Arrays.sort(copy);
        return copy;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }
}
