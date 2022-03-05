package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;

import org.meeuw.math.Randomizable;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * An abstract super structure for various integer types.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public abstract class AbstractIntegers<
    E extends AbstractIntegerElement<E, SIZE, S>,
    SIZE extends Scalar<SIZE>,
    S extends AbstractIntegers<E, SIZE, S>
    >
    extends AbstractAlgebraicStructure<E>  implements
    Streamable<E> ,
    Randomizable<E> {

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

    /**
     * The internal way to create a new element
    */
    abstract E of(BigInteger value);


    /*
     * @throws InvalidElementCreationException if the given backing value does not fit in the structure. E.g. the structure is {@link PositiveIntegers}, and the value is negative.
     */
    public abstract E newElement(BigInteger value) throws InvalidElementCreationException;
}
