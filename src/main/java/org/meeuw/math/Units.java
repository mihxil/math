package org.meeuw.math;

import java.util.Arrays;

import static org.meeuw.math.SIUnits.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class Units {

    public static final Units DISTANCE = Units.of(m);
    public static final Units LENGTH = Units.of(m);
    public static final Units AREA = Units.of(m, m);
    public static final Units VOLUME = Units.of(m, m, m);
    public static final Units TIME = Units.of(s);
    public static final Units SPEED = DISTANCE.dividedBy(TIME);

    public static final Units WEIGHT = Units.of(kg);
    public static final Units TEMPERATURE = Units.of(K);
    public static final Units ELECTRIC_CURRENT = Units.of(A);
    public static final Units AMOUNT_OF_SUBSTANCE = Units.of(mol);
    public static final Units LUMINOUS_INTENSITY = Units.of(cd);

    private final int[] basic = new int[values().length];

    public Units(SIUnits... units) {
        for (SIUnits u : units) {
            basic[u.ordinal()]++;
        }
    }

    public static Units of(SIUnits... units) {
        return new Units(units);
    }

    public Units times(Units units) {
        Units copy = copy();
        for (int i = 0; i < basic.length; i++) {
            copy.basic[i] += units.basic[i];
        }
        return copy;
    }
    public Units dividedBy(Units units) {
        Units copy = copy();
        for (int i = 0; i < basic.length; i++) {
            copy.basic[i] -= units.basic[i];
        }
        return copy;
    }
    public Units pow(int e) {
        Units copy = copy();
        for (int i = 0; i < basic.length; i++) {
            copy.basic[i] *= e;
        }
        return copy;
    }

    public Units copy() {
        Units copy = new Units();
        for (int i = 0; i < basic.length; i++) {
            copy.basic[i] = basic[i];
        }
        return copy;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < basic.length; i++) {
            int b = basic[i];
            if (b != 0) {
                builder.append(values()[i].name());
                if (b != 1) {
                    builder.append(Utils.superscript(b));
                }
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Units units = (Units) o;

        return Arrays.equals(basic, units.basic);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(basic);
    }
}
