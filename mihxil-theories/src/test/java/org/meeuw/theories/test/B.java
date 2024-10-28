package org.meeuw.theories.test;

public interface B extends A {

    int getJ();

    default int plus(B o) {
        return o == null ? getJ() : o.getJ() + getJ();

    }
}
