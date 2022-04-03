package org.meeuw.test.math.sample;

import org.meeuw.math.abstractalgebra.AdditiveGroup;
import org.meeuw.math.abstractalgebra.Cardinality;

public class TestGroup implements AdditiveGroup<TestElement> {

    public static TestGroup instance = new TestGroup();

    @Override
    public TestElement zero() {
        return TestElement.zero;
    }

    @Override
    public Cardinality getCardinality() {
        return new Cardinality(5);
    }

    @Override
    public Class<TestElement> getElementClass() {
        return TestElement.class;
    }

}
