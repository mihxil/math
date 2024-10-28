package org.meeuw.theories.test;

public class BImpl extends AImpl implements B {

    private final int j;

    public BImpl(int i, int j) {
        super(i);
        this.j = j;
    }

    @Override
    public int getJ() {
        return j;
    }
}
