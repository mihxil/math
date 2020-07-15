package org.meeuw.math.physics;

import java.util.*;

import org.meeuw.math.Utils;

/**
 * Represents the units of a {@link UncertainNumber}.
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
    public Units times(Units units) {
        List<UnitExponent> base = new ArrayList<>(Arrays.asList(exponents));
        OUTER:
        for (UnitExponent u : units) {
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
    public Units pow(int e) {
        List<UnitExponent> base = new ArrayList<>(Arrays.asList(exponents));
        for (int i = 0 ; i < base.size(); i++) {
            UnitExponent n = base.get(i);
            base.set(i, n.pow(e));
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
    public Iterator<UnitExponent> iterator() {
        return Arrays.stream(exponents).iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (UnitExponent e :exponents) {
            if (e.exponent != 0) {
                if (builder.length() > 0) {
                    builder.append(Utils.TIMES);
                }

                builder.append(e.toString());
            }
        }
        return builder.toString();
    }
}
