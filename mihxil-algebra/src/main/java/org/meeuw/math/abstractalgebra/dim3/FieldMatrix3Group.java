package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.*;

/**
 * A square 3x3 matrix of any {@link NumberFieldElement}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3Group<F extends NumberFieldElement<F>> implements
    MultiplicativeGroup<FieldMatrix3<F>> {

    private final NumberField<F> elementStructure;

    FieldMatrix3Group(NumberField<F> elementStructure) {
        this.elementStructure = elementStructure;

    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public FieldMatrix3<F> one() {
        F one = elementStructure.one();
        F zero = elementStructure.zero();
        return new FieldMatrix3<>((F[][]) new Object[][]{
            {one, zero, zero},
            {zero, one, zero},
            {zero, zero, one}
        });

    }
}
