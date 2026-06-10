package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Solid<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>, SELF extends Solid<F, C, SELF>>  {

    /**
     * @return volume of the solid. If calculation doesn't involve things like pi, there may also be a method {@code F exactVolume()}
     */
    C volume();

    /**
     * @return surface area of the solid. If calculation doesn't involve things like pi, there may also be a method {@code F exactSurfaceArea()}
     */
    C surfaceArea();

    boolean eq(SELF other);
}
