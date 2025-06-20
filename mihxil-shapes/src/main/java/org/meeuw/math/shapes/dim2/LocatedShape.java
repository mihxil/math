package org.meeuw.math.shapes.dim2;

import lombok.EqualsAndHashCode;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

import static org.meeuw.math.abstractalgebra.dim2.FieldVector2.origin;


/**
 * Normally shapes only have a size, and are defined at the origin. E.g. {@link Polygon#vertices()}} returns the vertices of the polygon centered at the origin. By combining a shape with a location, we can define a shape at any location in the plane.
 * <p>
 * This, for example, is also needed by calls like {@link Shape#circumscribedRectangle()} which returns a rectangle that may not be exactly centered at the origin.
 */
@EqualsAndHashCode
public class LocatedShape<F extends ScalarFieldElement<F>, S extends Shape<F, S>> {

    private final S shape;
    private final FieldVector2<F> location;

    public LocatedShape(S shape, FieldVector2<F> location) {
        this.shape = shape;
        this.location = location;
    }

    /**
     * A located shape with the origin as location.
     */
    private LocatedShape(S shape) {
        this(shape, origin(shape.field()));
    }

    public static <F extends ScalarFieldElement<F>, S extends Shape<F, S>> LocatedShape<F, S> atOrigin(S shape) {
        return new LocatedShape<>(shape);
    }

    public S shape() {
        return shape;
    }

    public FieldVector2<F> location() {
        return location;
    }

    @Override
    public String toString() {
        return shape() + (location.isZero() ? "" : (" at " + location()));
    }
}
