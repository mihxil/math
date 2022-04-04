package org.meeuw.test.math.sample;

import org.meeuw.math.abstractalgebra.AdditiveSemiGroup;
import org.meeuw.math.abstractalgebra.Cardinality;

public class AStruct implements AdditiveSemiGroup<A> {

    @Override
    public Cardinality getCardinality() {
        return new Cardinality(2);
    }

    @Override
    public Class<A> getElementClass() {
        return A.class;
    }
}
