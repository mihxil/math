package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * SO(3) group
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RotationGroup implements MultiplicativeGroup<Rotation, RotationGroup> {

    public static RotationGroup INSTANCE = new RotationGroup();

    private RotationGroup() {

    }
    @Override
    public Rotation one() {
        return new Rotation(new double[][]{
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        });
    }
}
