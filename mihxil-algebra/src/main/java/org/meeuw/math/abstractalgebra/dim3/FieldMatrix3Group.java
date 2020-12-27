package org.meeuw.math.abstractalgebra.dim3;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.meeuw.math.abstractalgebra.*;

/**
 * A square 3x3 matrix of any {@link ScalarFieldElement}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3Group<E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<FieldMatrix3<E>>
    implements MultiplicativeGroup<FieldMatrix3<E>> {


    public static final Map<ScalarField<?>, FieldMatrix3Group<?>> INSTANCES = new HashMap<>();

    @Getter
    private final ScalarField<E> elementStructure;

    private final FieldMatrix3<E> one;

    public static <E extends ScalarFieldElement<E>> FieldMatrix3Group<E> of(ScalarField<E> elementStructure) {
        return (FieldMatrix3Group<E>) INSTANCES.computeIfAbsent(elementStructure, (es) -> new FieldMatrix3Group<>(es));
    }

    private  FieldMatrix3Group(ScalarField<E> elementStructure) {
        super((Class) FieldMatrix3.class);
        this.elementStructure = elementStructure;
        E eOne = elementStructure.one();
        E eZero = elementStructure.zero();
        one =  FieldMatrix3.of(
            eOne, eZero, eZero,
            eZero, eOne, eZero,
            eZero, eZero, eOne);
    }

    @Override
    public FieldMatrix3<E> one() {
        return one;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.C; //elementStructure.getCardinality();
    }

}
