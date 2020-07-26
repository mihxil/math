package org.meeuw.physics;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum Dimension {
    L("length"),
    M("mass"),
    T("time"),
    I("electric current"),
    TH('\u0398', "thermodynamic temperature"),
    N("amount of substance"),
    J("luminous intensity");

    static int NUMBER = values().length;

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

    public String toString() {
        return toString == null ? name() : toString;
    }
}
