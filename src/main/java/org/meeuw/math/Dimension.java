package org.meeuw.math;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public enum Dimension {


    T,
    L,
    M,
    I,
    TH((char) 0x0398),
    N,
    J
    ;

    final String toString;
    Dimension(char i) {
        toString = String.valueOf(i);
    }
    Dimension() {
        toString = null;
    }

    public String toString() {
        return toString == null ? name() : toString;

    }
}
