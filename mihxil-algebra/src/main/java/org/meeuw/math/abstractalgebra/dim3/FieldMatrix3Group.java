package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3Group<F extends NumberFieldElement<F, A>, A extends NumberField<F, A>> implements MultiplicativeGroup<FieldMatrix3<F, A>, FieldMatrix3Group<F, A>> {

    private final A structure;

    FieldMatrix3Group(A structure) {
        this.structure = structure;

    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public FieldMatrix3<F, A> one() {
        F one = structure.one();
        F zero = structure.zero();
        return new FieldMatrix3<>((F[][]) new Object[][]{
            {one, zero, zero},
            {zero, one, zero},
            {zero, zero, one}
        });

    }
}
