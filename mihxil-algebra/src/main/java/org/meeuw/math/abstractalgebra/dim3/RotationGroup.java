package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;

/**
 * SO(3) group
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
        return new Rotation(new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        });
    }

    @Override
    public Cardinality cardinality() {
        return RealField.INSTANCE.cardinality();
    }
}
