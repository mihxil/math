package org.meeuw.math;

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
    TH((char) 0x0398, "thermodynamic temperature"),
    N("amount of substance"),
    J("luminous intensity");

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
