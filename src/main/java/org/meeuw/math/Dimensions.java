package org.meeuw.math;

/**

 * @author Michiel Meeuwissen
 */
public class Dimensions extends AbstractEnumInts<Dimension, Dimensions> {


    public Dimensions(Dimension... dimensions) {
        super(Dimension.values(), dimensions);
    }

    public Dimensions(int[] exponents) {
        super(exponents);
    }

    public static Dimensions of(Dimension... dimensions) {
        return new Dimensions(dimensions);
    }

    public String toString() {
        return Utils.toString(Dimension.values(), exponents);
    }

    @Override
    Dimensions newInstance() {
        return new Dimensions();

    }
}
