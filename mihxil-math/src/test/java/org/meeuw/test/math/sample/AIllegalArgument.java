package org.meeuw.test.math.sample;

public class AIllegalArgument extends A {
    @Override
    public A plus(A summand) {
        throw new MyException("foo bar");
    }
}
