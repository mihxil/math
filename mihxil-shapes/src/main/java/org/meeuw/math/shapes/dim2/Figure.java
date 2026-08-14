package org.meeuw.math.shapes.dim2;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.shapes.Info;
import org.meeuw.math.shapes.Shape;

/**
 * A 2 dimensional {@link Shape}
 * @param <E> type of the {@link ScalarFieldElement} used for the coordinates
 * @param <C> completion of that type (needed for trigonometry)
 */
public interface Figure<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>>
    extends Shape<E, C, Figure<E, C>, Figure<C, C>> {

    C perimeter();

    C area();

    default Stream<Info> info() {
        return Stream.of(
            new Info(Info.Key.AREA, this::area),
            new Info(Info.Key.PERIMETER, this::perimeter),
            new Info(Info.Key.CIRCUMSCRIBED_RECTANGLE, this::circumscribedRectangle),
            new Info(Info.Key.CIRCUMSCRIBED_CIRCLE, this::circumscribedCircle)
        );
    }

    /**
     * Returns a {@link LocatedFigure located} (unrotated) rectangle that precisely contains this shape (after rotation by the given angle (in radians)).
     */
    LocatedFigure<C, C, Rectangle<C, C>> circumscribedRectangle();

    /**
     * Returns a {@link LocatedFigure located} circle that precisely contains this shape.
     */
    LocatedFigure<C, C, Circle<C, C>> circumscribedCircle();

    Figure<E, C> rotate(E angle);

    /**
     * TODO?
     * @return
     */
    @Override
    default String toStringWithUncertainty() {
        return toString();
    }


}
