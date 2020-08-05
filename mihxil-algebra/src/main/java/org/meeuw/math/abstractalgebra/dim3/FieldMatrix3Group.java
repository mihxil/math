package org.meeuw.math.abstractalgebra.dim3;

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

    private final NumberField<E> elementStructure;

    FieldMatrix3Group(NumberField<E> elementStructure) {
        super((Class) FieldMatrix3.class);
        this.elementStructure = elementStructure;
    }

    @Override
    public FieldMatrix3<E> one() {
        E one = elementStructure.one();
        E zero = elementStructure.zero();
        return FieldMatrix3.of(
            one, zero, zero,
            zero, one, zero,
            zero, zero, one
        );
    }

    @Override
    public Cardinality cardinality() {
        return elementStructure.cardinality();
    }
}
