package org.meeuw.physics;

import lombok.Getter;

import java.util.Arrays;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.text.UncertainNumberFormat;

/**

 * @author Michiel Meeuwissen
 */
public class Dimensions implements MultiplicativeGroupElement<Dimensions> {


    @Getter
    final int[] exponents = new int[Dimension.values().length];

    public Dimensions(Dimension... dimensions) {
        for (Dimension  v: dimensions) {
            exponents[v.ordinal()]++;
        }
    }

    public Dimensions(int[] exponents) {
        System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
    }

    public static Dimensions of(Dimension... dimensions) {
        return new Dimensions(dimensions);
    }

    public String toString() {
        return UncertainNumberFormat.toString(Dimension.values(), exponents);
    }


    @Override
    public DimensionsGroup getStructure() {
        return DimensionsGroup.INSTANCE;
    }

    @Override
    public Dimensions times(Dimensions multiplier) {
        Dimensions copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] += multiplier.exponents[i];
        }
        return copy;
    }
    @Override
    public Dimensions pow(int exponent) {
        Dimensions copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] *= exponent;
        }
        return copy;
    }

    public Dimensions copy() {
        return new Dimensions(exponents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimensions units = (Dimensions) o;

        return Arrays.equals(exponents, units.exponents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }

}
