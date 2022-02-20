package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractIntegers<E extends AlgebraicElement<E>> extends AbstractAlgebraicStructure<E> implements
    Streamable<E>  {

    protected AbstractIntegers(Class<E> clazz) {
        super(clazz);
    }
    protected AbstractIntegers() {
        super();
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL_AND_EQUALS;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
