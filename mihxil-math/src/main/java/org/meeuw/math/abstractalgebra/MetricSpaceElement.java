package org.meeuw.math.abstractalgebra;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.Sizeable;

/**
 * Elements of a metric space define a {@link #distanceTo(MetricSpaceElement)} other elements in the same {@link MetricSpace}
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> self reference
 * @param <S> the type of the distance
 */
public interface MetricSpaceElement<E extends MetricSpaceElement<E, S>, S extends Scalar<S>>
    extends Sizeable<S> {

    MetricSpace<E, S> getStructure();

    S distanceTo(E otherElement);

    @Override
    @NonAlgebraic
    default S abs() {
        return distanceTo(getStructure().zero());
    }

}
