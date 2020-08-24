package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;

/**
 * SO(3) group. A non-abelian multiplicative group.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RotationGroup extends AbstractAlgebraicStructure<Rotation> implements MultiplicativeGroup<Rotation> {

    public static final RotationGroup INSTANCE = new RotationGroup();

    private RotationGroup() {
        super(Rotation.class);
    }
    @Override
    public Rotation one() {
        return Rotation.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return RealField.INSTANCE.getCardinality();
    }

    @Override
    public Equivalence<Rotation> getEquivalence() {
        return (e1, e2) -> e1.rot.getStructure().getEquivalence().test(e1.rot, e2.rot);
    }
}
