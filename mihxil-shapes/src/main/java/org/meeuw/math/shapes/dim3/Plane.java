package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.dim3.FieldVector3;

import static java.util.Objects.requireNonNull;

public record Plane<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>>(FieldVector3<E, C> point, FieldVector3<E, C> normal) {

    public Plane(FieldVector3<E, C> point, FieldVector3<E, C> normal) {
        this.point = requireNonNull(point, "point must not be null");
        this.normal = requireNonNull(normal, "normal must not be null").normalize();
        assert normal.abs().isOne();
    }

    public FieldVector2<E, C> project(FieldVector3<E, C> vector3, FieldVector3<E, C> vantagePoint) {
        FieldVector3<E, C> direction = vector3.minus(vantagePoint);
        E dot = this.point.dot(this.normal);
        E t = dot.minus(normal().dot(vantagePoint)).dividedBy(normal().dot(direction));
        FieldVector3<E, C> intersection = vantagePoint.plus(direction.times(t));
        return new FieldVector2<E, C>(intersection.get(0), intersection.get(1));
    }
}
