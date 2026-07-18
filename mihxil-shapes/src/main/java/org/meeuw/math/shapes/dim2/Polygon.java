package org.meeuw.math.shapes.dim2;

import java.util.Iterator;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.Info;

public interface Polygon<F extends ScalarFieldElement<F, C>,  C extends CompleteScalarFieldElement<C>, SELF extends Figure<F, C, SELF>> extends Figure<F, C, SELF> {

    int numberOfEdges();

    default int numberOfVertices() {
        return numberOfEdges();
    }

    default Stream<Info> info() {
        return Stream.concat(
            Figure.super.info(),
            Stream.of(
                new Info(Info.Key.EDGES, this::numberOfEdges),
                new Info(Info.Key.VERTICES, this::numberOfVertices)
            )
        );
    }


    Stream<FieldVector2<C, C>> vertices();

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation of a polygon is based on {@link #vertices()}, and just finding the minimum and maximum x and y coordinates of those.
     */
    default LocatedShape<C, C, Rectangle<C, C>> circumscribedRectangle() {
        ScalarField<F, C> field = field();
        Iterator<FieldVector2<C, C>> vertices = vertices().iterator();
        FieldVector2<C, C> first = vertices.next();
        C minX  = first.getX();
        C maxX  = first.getX();
        C minY  = first.getY();
        C maxY  = first.getY();
        while(vertices.hasNext()) {
            FieldVector2<C, C> v = vertices.next();
            C x = v.getX();
            C y = v.getY();
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
        C centerX = minX.plus(maxX).dividedBy(2);
        C centerY = minY.plus(maxY).dividedBy(2);
        FieldVector2<C, C> center = FieldVector2.of(centerX, centerY);
        return new LocatedShape<>(new Rectangle<>(
            maxX.minus(minX),
            maxY.minus(minY),
            field.zero().complete()),
            center
        );
    }

}
