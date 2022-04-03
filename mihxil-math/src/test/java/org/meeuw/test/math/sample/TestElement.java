package org.meeuw.test.math.sample;

import org.meeuw.math.abstractalgebra.AdditiveGroupElement;

public class TestElement implements AdditiveGroupElement<TestElement> {

    public final static TestElement zero = new TestElement(null);

    private final TestElement negation;

    public TestElement(TestElement negation) {
        this.negation = negation == null ? this : negation;
    }

    @Override
    public TestGroup getStructure() {
        return TestGroup.instance;
    }

    @Override
    public TestElement negation() {
        return negation;
    }

    @Override
    public TestElement plus(TestElement summand) {
        return null;
    }

    @Override
    public String toString() {
        return "testelement";
    }
}
