package org.meeuw.math.shapes.dim2;

import java.util.stream.Stream;

import org.meeuw.functional.Suppliers;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
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
            new Info(Info.Key.AREA, Suppliers.wrap(this::area)),
            new Info(Info.Key.PERIMETER, Suppliers.wrap(this::perimeter)),
            new Info(Info.Key.CIRCUMSCRIBED_RECTANGLE, Suppliers.wrap(() -> circumscribedRectangle().shape().parametersString())),
            new Info(Info.Key.CIRCUMSCRIBED_CIRCLE, Suppliers.wrap(() -> circumscribedCircle().shape().parametersString()))
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
