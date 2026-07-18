package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.Shape;

/**
 * A 3 dimensional {@link Shape}
 * @param <F> Type of the coordinates
 * @param <C> Completion of that type
 * @param <SELF> Type of the solid itself
 */
public interface Solid<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>, SELF extends Solid<F, C, SELF>>  extends Shape<F, C, SELF> {

    /**
     * @return volume of the solid. If calculation doesn't involve things like pi, there may also be a method {@code F exactVolume()}
     */
    C volume();

    /**
     * @return surface area of the solid. If calculation doesn't involve things like pi, there may also be a method {@code F exactSurfaceArea()}
     */
    C surfaceArea();

    /**
     * Returns the number of holes in this solid. For example, a torus has 1 hole, a sphere has 0 holes.
     */
    default int holes() {
        return 0;
    }


    boolean eq(SELF other);
}
