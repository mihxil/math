package org.meeuw.test.math.sample;

import java.util.function.BinaryOperator;

import org.meeuw.math.abstractalgebra.AdditiveGroupElement;

public class SampleElement implements AdditiveGroupElement<SampleElement> {

    public static ThreadLocal<BinaryOperator<SampleElement>> PLUS = ThreadLocal.withInitial(() -> (a, b) -> a);


    private final BinaryOperator<SampleElement> p;

    public SampleElement() {
        this(PLUS.get());
    }

    public SampleElement(BinaryOperator<SampleElement> p) {
        this.p = p;
    }

    @Override
    public SampleStructure getStructure() {
        return new SampleStructure();
    }

    @Override
    public SampleElement plus(SampleElement summand) {
        return p.apply(this, summand);
    }

    @Override
    public SampleElement negation() {
        return null;
    }

    @Override
    public String toString() {
        return "sampleelement";
    }

}
