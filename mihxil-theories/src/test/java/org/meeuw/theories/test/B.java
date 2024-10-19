package org.meeuw.theories.test;

public class B extends A implements Comparable<B> {
    public B(int i, String s) {
        super(i, s);
    }

    @Override
    public int compareTo(B o) {
        return 0;
    }
}
