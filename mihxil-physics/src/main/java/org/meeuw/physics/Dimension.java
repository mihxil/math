package org.meeuw.physics;

import lombok.Getter;

/**
 * Representation of a basic physical dimension. This follows the SI recommendation.
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum Dimension {


    L("length"),
    M("mass"),
    T("time"),
    I("electric current"),
    Θ('\u0398', "thermodynamic temperature"),
    N("amount of substance"),
    J("luminous intensity");

    /**
     * Just an alias if you can't type greek
     */
    static final Dimension TH = Θ;

    static final int NUMBER = values().length;

    final String toString;

    @Getter
    final String name;

    Dimension(char i, String name) {
        toString = String.valueOf(i);
        this.name = name;
    }

    Dimension(String name) {
        this.name = name;
        toString = null;
    }

    @Override
    public String toString() {
        return toString == null ? name() : toString;
    }
}
