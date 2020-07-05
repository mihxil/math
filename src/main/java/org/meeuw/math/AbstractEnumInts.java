package org.meeuw.math;

import java.util.Arrays;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public abstract class AbstractEnumInts<T extends Enum<T>, S extends AbstractEnumInts<T, S>> {

    final int[] exponents;

    @SafeVarargs
    protected AbstractEnumInts(T[] possibleValues, T... values) {
        this.exponents = new int[possibleValues.length];
        for (T  v: values) {
            exponents[v.ordinal()]++;
        }
    }

    protected AbstractEnumInts(int[] exponents) {
        this.exponents = exponents;

    }


    public S times(S units) {
        S copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] += units.exponents[i];
        }
        return copy;
    }
    public S dividedBy(S units) {
        S copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] -= units.exponents[i];
        }
        return copy;
    }
    public S pow(int e) {
        S copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] *= e;
        }
        return copy;
    }

     public S copy() {
        S copy = newInstance();
        System.arraycopy(exponents, 0, copy.exponents, 0, exponents.length);
        return copy;
    }

    abstract S newInstance();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEnumInts<?, ?> units = (AbstractEnumInts) o;

        return Arrays.equals(exponents, units.exponents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }

}
