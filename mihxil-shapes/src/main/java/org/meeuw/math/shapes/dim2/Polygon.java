package org.meeuw.math.shapes.dim2;

import java.util.Iterator;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.dim2.Rotation2Group;
import org.meeuw.math.exceptions.FieldIncompleteException;

public interface Polygon<F extends ScalarFieldElement<F>, SELF extends Shape<F, SELF>> extends Shape<F, SELF>   {

    int numberOfEdges();

    default int numberOfVertices() {
        return numberOfEdges();
    }

    default Stream<String[]> info() {
        return Stream.concat(
            Shape.super.info(),
            Stream.of(
                new String[]{"# edges", Integer.toString(numberOfEdges())},
                new String[]{"# vertices", Integer.toString(numberOfVertices())}
            )
        );
    }

    Stream<FieldVector2<F>> vertices();

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation of a polygon is based on {@link #vertices()}, and just finding the minimum and maximum x and y coordinates of those.
     */
    default LocatedShape<F, Rectangle<F>> circumscribedRectangle(@radians F angle) {
        ScalarField<F> field = field();
        if ( ! (field instanceof CompleteScalarField) && ! angle.isZero()) {
            throw new FieldIncompleteException("Cannot compute circumscribed rectangle for " + this + " with angle " + angle + " in field " + field);
        }
        CompleteScalarField<?> completeField = (CompleteScalarField) field;
        Rotation2Group<?> rotationGroup = Rotation2Group.of(completeField);
        //Rotation2<?> rotation = rotationGroup.rotation((CompleteScalarFieldElement) angle);



        //Iterator<FieldVector2<F>> vertices = vertices().map(f -> f.times(rotation)).iterator();
        Iterator<FieldVector2<F>> vertices = vertices().iterator();
        FieldVector2<F> first = vertices.next();
        F minX  = first.getX();
        F maxX  = first.getX();
        F minY  = first.getY();
        F maxY  = first.getY();
        while(vertices.hasNext()) {
            FieldVector2<F> v = vertices.next();
            F x = v.getX();
            F y = v.getY();
            if (x.compareTo(minX) < 0) {
                minX = x;
            } else if (x.compareTo(maxX) > 0) {
                maxX = x;
            }
            if (y.compareTo(minY) < 0) {
                minY = y;
            } else if (y.compareTo(maxY) > 0) {
                maxY = y;
            }
        }
        F centerX = minX.plus(maxX).dividedBy(2);
        F centerY = minY.plus(maxY).dividedBy(2);
        FieldVector2<F> center = FieldVector2.of(centerX, centerY);
        return new LocatedShape<>(new Rectangle<>(
            maxX.minus(minX),
            maxY.minus(minY)),
            center
        );
    }

}
