package org.meeuw.math.abstractalgebra.permutations.text;

import java.util.function.IntSupplier;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public enum Offset implements IntSupplier {
    ZERO(0),
    ONE(1);

    final int i;

    Offset(int i) {
        this.i = i;
    }

    @Override
    public int getAsInt() {
        return i;
    }
}
