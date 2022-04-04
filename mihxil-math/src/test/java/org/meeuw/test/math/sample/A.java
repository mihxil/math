package org.meeuw.test.math.sample;

import org.meeuw.math.abstractalgebra.AdditiveSemiGroup;
import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;

public class A implements AdditiveSemiGroupElement<A> {

    @Override
    public AdditiveSemiGroup<A> getStructure() {
        return new AStruct();
    }

    @Override
    public A plus(A summand) {
        return new A();
    }

    @Override
    public String toString() {
        return "<a>";
    }

    public A dividedBy(A divisor) {
        return new A();
    }
}
