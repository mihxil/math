package org.meeuw.theories.test;

import java.io.Serializable;

public class A implements Serializable {
    private final int i;
    private final String s;

    public A(int i, String s) {
        this.i = i;
        this.s = s;
    }

}
