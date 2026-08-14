package org.meeuw.math.shapes.dim2;

import lombok.EqualsAndHashCode;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

import static org.meeuw.math.abstractalgebra.dim2.FieldVector2.origin;


/**
 * Normally shapes only have a size, and are defined at the origin. E.g. {@link Polygon#vertices()}} returns the vertices of the polygon centered at the origin. By combining a shape with a location, we can define a shape at any location in the plane.
 * <p>
 * This, for example, is also needed by calls like {@link Figure#circumscribedRectangle()} which returns a rectangle that may not be exactly centered at the origin.
 */
@EqualsAndHashCode
public class LocatedFigure<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>, S extends Figure<F, C>> {

    private final S shape;
    private final FieldVector2<F, C> location;

    public LocatedFigure(S shape, FieldVector2<F, C> location) {
        this.shape = shape;
        this.location = location;
    }

    /**
     * A located shape with the origin as location.
     */
    private LocatedFigure(S shape) {
        this(shape, origin(shape.field()));
    }

    public static <
        F extends ScalarFieldElement<F, C>,
        C extends CompleteScalarFieldElement<C>,
        S extends Figure<F, C>> LocatedFigure<F, C, S> atOrigin(S shape) {
        return new LocatedFigure<>(shape);
    }

    public S shape() {
        return shape;
    }

    public <S2 extends Figure<C, C>> LocatedFigure<C, C, S2> complete() {
        S2 complete = (S2) shape().complete();
        return new LocatedFigure<C, C, S2>(complete, location.complete());
    }


    public FieldVector2<F, C> location() {
        return location;
    }

    @Override
    public String toString() {
        return shape() + (location.isZero() ? "" : (" at " + location()));
    }
}
