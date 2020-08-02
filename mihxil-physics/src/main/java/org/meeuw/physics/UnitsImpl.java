package org.meeuw.physics;

import org.meeuw.math.AbstractUncertainNumber;
import org.meeuw.math.text.UncertainNumberFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    public UnitsGroup structure() {
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
                    continue OUTER;
                }
            }
            base.add(u);
        }
        return new UnitsImpl(base.toArray(new UnitExponent[0]));
    }

    @Override
    public Units pow(int exponent) {
        List<UnitExponent> base = new ArrayList<>(Arrays.asList(exponents));
        for (int i = 0 ; i < base.size(); i++) {
            UnitExponent n = base.get(i);
            base.set(i, n.pow(exponent));
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
        return new PhysicalConstant(0, this, "zero " + toString());
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

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(exponents, that.exponents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }
}
