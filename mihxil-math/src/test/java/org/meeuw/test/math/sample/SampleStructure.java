package org.meeuw.test.math.sample;

import org.meeuw.math.abstractalgebra.AdditiveGroup;
import org.meeuw.math.abstractalgebra.Cardinality;

public class SampleStructure implements AdditiveGroup<SampleElement> {

    @Override
    public Cardinality getCardinality() {
        return new Cardinality(1);
    }

    @Override
    public Class<SampleElement> getElementClass() {
        return SampleElement.class;
    }

    @Override
    public SampleElement zero() {
        return new SampleElement();
    }
}
