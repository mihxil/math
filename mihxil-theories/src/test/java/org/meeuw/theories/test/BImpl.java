package org.meeuw.theories.test;

import lombok.ToString;

@ToString
public class BImpl extends AImpl implements B {

    private final int j;

    public BImpl(int j) {

        this.j = j;
    }

    @Override
    public int getJ() {
        return j;
    }
}
