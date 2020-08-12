package org.meeuw.math.abstractalgebra.dim3;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.meeuw.math.abstractalgebra.*;

/**
 * A square 3x3 matrix of any {@link NumberFieldElement}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3Group<E extends NumberFieldElement<E>>
    extends AbstractAlgebraicStructure<FieldMatrix3<E>>
    implements MultiplicativeGroup<FieldMatrix3<E>> {


    public static final Map<NumberField, FieldMatrix3Group> INSTANCES = new HashMap<>();

    @Getter
    private final NumberField<E> elementStructure;

    private final FieldMatrix3<E> one;

    public static <E extends NumberFieldElement<E>> FieldMatrix3Group<E> of(NumberField<E> elementStructure) {
        return INSTANCES.computeIfAbsent(elementStructure, (es) -> new FieldMatrix3Group(es));
    }

    private  FieldMatrix3Group(NumberField<E> elementStructure) {
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
        return elementStructure.getCardinality();
    }
}
