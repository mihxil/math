package org.meeuw.physics;

import lombok.Getter;

import java.util.*;

import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.AbstractUncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * Represents the units of a {@link AbstractUncertainDouble}.
 *
 * Basicly keeps track of a integer power for each of the basic SI units.
 *
 * @author Michiel Meeuwissen
 */
public class UnitsImpl implements Units  {

    @Getter
    private final UncertainReal SIFactor;
    @Getter
    private final UnitExponent[] exponents;

    public UnitsImpl(UncertainReal siFactor, Unit... units) {
        this(siFactor, Unit.toArray(units));
    }

    public UnitsImpl(UncertainReal siFactor, UnitExponent... units) {
        this.exponents = units;
        this.SIFactor = siFactor;
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
        return new UnitsImpl(SIFactor.times(multiplier.getSIFactor()), base.toArray(new UnitExponent[0]));
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
        return new UnitsImpl(SIFactor.pow(exponent), base.toArray(new UnitExponent[0]));
    }

    @Override
    public Dimensions getDimensions() {
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
        return FormatService.toString(this);
    }

}
